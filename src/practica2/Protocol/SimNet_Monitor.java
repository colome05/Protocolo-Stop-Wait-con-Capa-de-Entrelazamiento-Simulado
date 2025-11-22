package practica2.Protocol;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import practica1.CircularQ.CircularQueue;
import util.Const;
import util.TCPSegment;
import util.SimNet;

public class SimNet_Monitor implements SimNet {

  protected CircularQueue<TCPSegment> queue;
  protected ReentrantLock lock;
  protected Condition cond;

  public SimNet_Monitor() {
    queue  = new CircularQueue<>(Const.SIMNET_QUEUE_SIZE);
    lock = new ReentrantLock();
    cond = lock.newCondition();
  }

  @Override
  public void send(TCPSegment seg) {
    lock.lock();
    try {
    
        while(queue.full()) {
        
            cond.awaitUninterruptibly();
        
        }
        
        queue.put(seg);
        cond.signal();
    
    } finally {
    
        lock.unlock();
    
    }
  }

  @Override
  public TCPSegment receive() {
    
    lock.lock();
    try {
        
        while(queue.empty()) {
        
            cond.awaitUninterruptibly();
        
        }

        cond.signal();
        return queue.get();
    
    } finally {
    
        lock.unlock();
    
    }
      
  }

  @Override
  public int getMTU() {
    throw new UnsupportedOperationException("Not supported yet. NO cal completar fins a la pràctica 3...");
  }

}
