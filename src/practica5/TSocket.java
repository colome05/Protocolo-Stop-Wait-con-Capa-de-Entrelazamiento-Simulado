package practica5;

import practica1.CircularQ.CircularQueue;
import practica4.Protocol;
import util.Const;
import util.TSocket_base;
import util.TCPSegment;

public class TSocket extends TSocket_base {

    // Sender variables:
    protected int MSS;
    protected int snd_sndNxt; //proper n de sequencia
    protected int snd_rcvWnd;
    protected int snd_rcvNxt; //segment que espera el receptor
    protected boolean zero_wnd_probe_ON;

    // Receiver variables:
    protected CircularQueue<TCPSegment> rcv_Queue;
    protected int rcv_SegConsumedBytes;
    protected int rcv_rcvNxt;

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
        rcv_Queue = new CircularQueue<>(2);

    }

    // -------------  SENDER PART  ---------------
    @Override
    public void sendData(byte[] data, int offset, int length) {
        lock.lock();
        try {
            int numBytes = 0;

            while (length > 0) {
                
                while(snd_sndNxt - snd_rcvNxt >= snd_rcvWnd) {
                    appCV.awaitUninterruptibly();
                }

                numBytes = Math.min(MSS, length);
                
                if(snd_rcvWnd == 0) {
                
                    numBytes = 1;
                    zero_wnd_probe_ON = true;
                    log.printPURPLE("------------ zero_wnd_probe_ON -----------");
                    
                }

                TCPSegment seg = segmentize(data, offset, numBytes);
                network.send(seg);
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
            
            if(seg.getSeqNum() >= snd_rcvNxt) {
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
                a_agafar = a_agafar + consumeSegment(buf, offset + a_agafar, maxlen - a_agafar);
            }

            return a_agafar;
        } finally {
            lock.unlock();
        }
    }

    protected int consumeSegment(byte[] buf, int offset, int length) {
        TCPSegment seg = rcv_Queue.peekFirst();
        int a_agafar = Math.min(length, seg.getDataLength() - rcv_SegConsumedBytes);
        System.arraycopy(seg.getData(), rcv_SegConsumedBytes, buf, offset, a_agafar);
        rcv_SegConsumedBytes += a_agafar;
        if (rcv_SegConsumedBytes == seg.getDataLength()) {
            rcv_Queue.get();
            rcv_SegConsumedBytes = 0;
        }
        return a_agafar;
    }

    protected void sendAck() {
        TCPSegment ack = new TCPSegment();
        ack.setAck(true);
        ack.setAckNum(rcv_rcvNxt); //snd_rcvNxt
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
                    log.printPURPLE("------------ zero_wnd_probe_OFF -----------");
                }
            }
            if (rseg.isPsh()) {
                if (!rcv_Queue.full()) {
                    if (rcv_rcvNxt == rseg.getSeqNum()) {
                        rcv_rcvNxt = rcv_rcvNxt + 1;
                        sendAck();
                        rcv_Queue.put(rseg);
                        appCV.signalAll();
                    } else if (rcv_rcvNxt > rseg.getSeqNum()) {
                        log.printRED("Paquet repetit");
                        sendAck();
                    } else if (rcv_rcvNxt < rseg.getSeqNum()) {
                        log.printRED("Paquet nou (descartat)");
                    }
                } else {
                    log.printRED("Paquet perdut a la xarxa (cua plena)");
                }
            }
        } finally {
            lock.unlock();
        }
    }
}
