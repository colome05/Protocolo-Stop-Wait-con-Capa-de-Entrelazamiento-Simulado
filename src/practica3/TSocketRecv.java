package practica3;

import practica1.CircularQ.CircularQueue;
import util.Const;
import util.TCPSegment;
import util.TSocket_base;
import util.SimNet;

public class TSocketRecv extends TSocket_base {

  protected Thread thread;
  protected CircularQueue<TCPSegment> rcvQueue;
  protected int rcvSegConsumedBytes;

  public TSocketRecv(SimNet network) {
    super(network);
    rcvQueue = new CircularQueue<>(Const.RCV_QUEUE_SIZE);
    rcvSegConsumedBytes = 0;
    new ReceiverTask().start();
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

  @Override
  public void processReceivedSegment(TCPSegment rseg) {  
    lock.lock();
    try {
      
        if(!this.rcvQueue.full()) {
            this.rcvQueue.put(rseg);
            printRcvSeg(rseg);
            appCV.signal();
        }
        
        
    } finally {
      lock.unlock();
    }
  }

  class ReceiverTask extends Thread {

    @Override
    public void run() {
      while (true) {
        TCPSegment rseg = network.receive();
        processReceivedSegment(rseg);
      }
    }
  }
}
