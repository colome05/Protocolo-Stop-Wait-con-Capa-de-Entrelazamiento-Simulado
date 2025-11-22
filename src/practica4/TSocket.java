package practica4;

import practica1.CircularQ.CircularQueue;
import util.Const;
import util.TCPSegment;
import util.TSocket_base;

public class TSocket extends TSocket_base {

  //sender variable:
  protected int MSS;

  //receiver variables:
  protected CircularQueue<TCPSegment> rcvQueue;
  protected int rcvSegConsumedBytes;

  protected TSocket(Protocol p, int localPort, int remotePort) {
    super(p.getNetwork());
    this.localPort  = localPort;
    this.remotePort = remotePort;
    p.addActiveTSocket(this);
    MSS = network.getMTU() - Const.IP_HEADER - Const.TCP_HEADER;
    rcvQueue = new CircularQueue<>(Const.RCV_QUEUE_SIZE);
    rcvSegConsumedBytes = 0;
  }

  @Override
  public void sendData(byte[] data, int offset, int length) {
    
        int numBytes = 0;

        while (length > 0) {

            numBytes = Math.min(MSS, length);

            TCPSegment seg = segmentize(data, offset, numBytes);
            network.send(seg);
            printSndSeg(seg);

            offset = offset + numBytes;
            length = length - numBytes;
        }
  }

  protected TCPSegment segmentize(byte[] data, int offset, int length) {
    TCPSegment fragment = new TCPSegment();
        fragment.setData(data, offset, length);
        fragment.setSourcePort(localPort);
        fragment.setDestinationPort(remotePort);
        fragment.setPsh(true);
        return fragment;
  }

  @Override
  public int receiveData(byte[] buf, int offset, int length) {
    lock.lock();
    try {
      while(this.rcvQueue.empty()){
          appCV.awaitUninterruptibly();
      }
      
      int a_agafar = 0;
      
      while (a_agafar < length && !rcvQueue.empty()) {
            a_agafar = a_agafar + consumeSegment(buf, offset + a_agafar, length - a_agafar);
        }
      
      return a_agafar;
      
    } finally {
      lock.unlock();
    }
  }

  protected int consumeSegment(byte[] buf, int offset, int length) {
    TCPSegment seg = rcvQueue.peekFirst();
    int a_agafar = Math.min(length, seg.getDataLength() - rcvSegConsumedBytes);
    System.arraycopy(seg.getData(), rcvSegConsumedBytes, buf, offset, a_agafar);
    rcvSegConsumedBytes += a_agafar;
    if (rcvSegConsumedBytes == seg.getDataLength()) {
      rcvQueue.get();
      rcvSegConsumedBytes = 0;
    }
    return a_agafar;
  }

  protected void sendAck() {
    TCPSegment ack = new TCPSegment();
    ack.setAck(true);
    ack.setSourcePort(localPort);
    ack.setDestinationPort(remotePort);
    printSndSeg(ack);
    network.send(ack);
  }

  @Override
  public void processReceivedSegment(TCPSegment rseg) {

    lock.lock();
    try {
      printRcvSeg(rseg);
            if (rseg.isAck()) {
                //no hago nada
            }
            if (rseg.isPsh()) {
                if (!rcvQueue.full()) {
                    sendAck();
                    rcvQueue.put(rseg);
                    appCV.signalAll();
                } else {
                    log.printRED("Paquete perdido en la red");
                }
            }
    } finally {
      lock.unlock();
    }
  }

}
