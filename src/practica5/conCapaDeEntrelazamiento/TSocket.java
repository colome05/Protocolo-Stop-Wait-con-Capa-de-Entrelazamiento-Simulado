package practica5.conCapaDeEntrelazamiento;

import practica1.CircularQ.CircularQueue;
import practica4.Protocol;
import util.Const;
import util.TSocket_base;
import util.TCPSegment;

public class TSocket extends TSocket_base {

    // Sender variables:
    protected int MSS;
    protected int snd_sndNxt;
    protected int snd_rcvWnd;
    protected int snd_rcvNxt;
    protected boolean zero_wnd_probe_ON;

    // Receiver variables:
    protected CircularQueue<TCPSegment> rcv_Queue;
    protected int rcv_SegConsumedBytes;
    protected int rcv_rcvNxt;

    // Quantum variables:
    // Usamos una semilla fija para que ambos generen la misma secuencia.
    // En la vida real, esto serían pares de fotones distribuidos antes de empezar.
    private java.util.Random quantumEntangledSource;

    protected TSocket(Protocol p, int localPort, int remotePort) {
        super(p.getNetwork());
        this.localPort = localPort;
        this.remotePort = remotePort;
        p.addActiveTSocket(this);

        // init sender variables
        MSS = p.getNetwork().getMTU() - Const.IP_HEADER - Const.TCP_HEADER;
        snd_rcvWnd = Const.RCV_QUEUE_SIZE;

        // init receiver variables
        //rcv_Queue = new CircularQueue<>(Const.RCV_QUEUE_SIZE);
        rcv_Queue = new CircularQueue<>(2); // Cola pequeña para forzar control de flujo

        // init Quantum variables
        this.quantumEntangledSource = new java.util.Random(9999);
    }

    // -------------  SENDER PART  ---------------
    @Override
    public void sendData(byte[] data, int offset, int length) {
        lock.lock();
        try {
            int numBytes = 0;

            while (length > 0) {

                while (snd_sndNxt - snd_rcvNxt >= snd_rcvWnd) {
                    appCV.awaitUninterruptibly();
                }

                numBytes = Math.min(MSS, length);

                if (snd_rcvWnd == 0) {
                    numBytes = 1;
                    zero_wnd_probe_ON = true;
                    log.printPURPLE("------------ QUANTUM CHANNEL BLOCKED (Zero Window) -----------");
                }

                // realiza la medición de Bell, 'payload' contiene resultados clásicos.
                byte[] classicalPayload = performBellMeasurement(data, offset, numBytes);

                TCPSegment seg = segmentize(classicalPayload, 0, classicalPayload.length);

                network.send(seg);
                

                // Como estos bits son clásicos, podemos retransmitirlos sin violar el teorema de no-clonación.
                startRTO(seg);
                printSndSeg(seg);

                offset = offset + numBytes;
                length = length - numBytes;

                snd_sndNxt = snd_sndNxt + 1;
            }
        } finally {
            lock.unlock();
        }
    }

    protected byte[] performBellMeasurement(byte[] data, int offset, int length) {
        byte[] measurements = new byte[length];

        for (int i = 0; i < length; i++) {
            // Obtenemos el "par entrelazado" local (byte semi-aleatorio)
            byte entangledPair = (byte) quantumEntangledSource.nextInt(256);

            // Simulamos la medición
            // En lógica clásica reversible, esto es un XOR (^). Cifrado de Vernam.
            measurements[i] = (byte) (data[offset + i] ^ entangledPair);
        }
        return measurements;
    }

    protected TCPSegment segmentize(byte[] data, int offset, int length) {
        TCPSegment fragment = new TCPSegment();
        fragment.setData(data, offset, length);
        fragment.setSeqNum(snd_sndNxt);
        fragment.setSourcePort(localPort);
        fragment.setDestinationPort(remotePort);
        fragment.setPsh(true);
        return fragment;
    }

    @Override
    protected void timeout(TCPSegment seg) {
        lock.lock();
        try {
            if (seg.getSeqNum() >= snd_rcvNxt) {
                log.printRED("Timeout: Retransmitiendo bits clásicos de teleportación !!!");
                network.send(seg);
                startRTO(seg);
                printSndSeg(seg);
            }
        } finally {
            lock.unlock();
        }
    }

    // -------------  RECEIVER PART  ---------------
    @Override
    public int receiveData(byte[] buf, int offset, int maxlen) {
        lock.lock();
        try {
            while (this.rcv_Queue.empty()) {
                appCV.awaitUninterruptibly();
            }

            int a_agafar = 0;

            while (a_agafar < maxlen && !rcv_Queue.empty()) {
                a_agafar = a_agafar + consumeSegmentAndReconstruct(buf, offset + a_agafar, maxlen - a_agafar);
            }

            return a_agafar;
        } finally {
            lock.unlock();
        }
    }

    protected int consumeSegmentAndReconstruct(byte[] buf, int offset, int length) {
        TCPSegment seg = rcv_Queue.peekFirst();
        int availableData = seg.getDataLength() - rcv_SegConsumedBytes;
        int a_agafar = Math.min(length, availableData);

        // Obtenemos los 'bits de corrección' (el payload cifrado/codificado) que llegaron por la red
        byte[] receivedCorrectionBits = seg.getData();

        for (int i = 0; i < a_agafar; i++) {

            // usamos la misma semilla que el emisor, este byte será idéntico al que el sender usó para codificar.
            byte localEntangledPair = (byte) quantumEntangledSource.nextInt(256);

            // OPERACIÓN DE PAULI (Reconstrucción):
            // Aplicamos XOR (Vernam) entre lo recibido y el par entrelazado.
            // Matemáticamente: (DatoOriginal ^ Clave) ^ Clave = DatoOriginal.
            int indexEnSegmento = rcv_SegConsumedBytes + i;
            buf[offset + i] = (byte) (receivedCorrectionBits[indexEnSegmento] ^ localEntangledPair);
        }

        rcv_SegConsumedBytes += a_agafar;

        if (rcv_SegConsumedBytes == seg.getDataLength()) {

            rcv_Queue.get();
            rcv_SegConsumedBytes = 0;
            sendAck();
            appCV.signalAll();
        }
        return a_agafar;
    }

    protected void sendAck() {
        TCPSegment ack = new TCPSegment();
        ack.setAck(true);
        ack.setAckNum(rcv_rcvNxt);
        ack.setWnd(rcv_Queue.free());
        ack.setSourcePort(localPort);
        ack.setDestinationPort(remotePort);
        printSndSeg(ack);
        network.send(ack);
    }

    // -------------  SEGMENT ARRIVAL  -------------
    @Override
    public void processReceivedSegment(TCPSegment rseg) {
        lock.lock();
        try {
            printRcvSeg(rseg);

            if (rseg.isAck()) {
                snd_rcvNxt = rseg.getAckNum();
                snd_rcvWnd = rseg.getWnd();
                appCV.signalAll();
                if (zero_wnd_probe_ON && (rseg.getWnd() != 0)) {
                    zero_wnd_probe_ON = false;
                    log.printPURPLE("------------ Quantum Channel Unblocked -----------");
                }
            }
            if (rseg.isPsh()) {
                if (!rcv_Queue.full()) {
                    if (rcv_rcvNxt == rseg.getSeqNum()) {
                        rcv_rcvNxt = rcv_rcvNxt + 1;
                        rcv_Queue.put(rseg);
                        sendAck();
                        appCV.signalAll();

                    } else if (rcv_rcvNxt > rseg.getSeqNum()) {
                        log.printRED("Corrección repetida (ACK perdido anteriormente)");
                        sendAck();
                    } else if (rcv_rcvNxt < rseg.getSeqNum()) {
                        log.printRED("Corrección fuera de orden (Descartada)");
                    }
                } else {
                    log.printRED("Buffer cuántico lleno. Paquete descartado.");
                }
            }
        } finally {
            lock.unlock();
        }
    }
}
