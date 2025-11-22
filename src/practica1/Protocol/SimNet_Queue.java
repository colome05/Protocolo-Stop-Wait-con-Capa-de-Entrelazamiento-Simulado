package practica1.Protocol;

import practica1.CircularQ.CircularQueue;
import util.Const;
import util.TCPSegment;
import util.SimNet;

public class SimNet_Queue implements SimNet {

  CircularQueue<TCPSegment> queue;

  public SimNet_Queue() {
    queue = new CircularQueue<>(Const.SIMNET_QUEUE_SIZE);
  }

  @Override
  public void send(TCPSegment s) {
    queue.put(s);
  }

  @Override
  public TCPSegment receive() {
    return queue.get();
  }

  @Override
  public int getMTU() {
    throw new UnsupportedOperationException("Not supported yet. No cal completar en aquesta pràctica");
  }
}
