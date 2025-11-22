package util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;

public abstract class Protocol_base {
  
  protected SimNet network;
  protected Lock lk;
  protected ArrayList<TSocket_base> listenSockets;
  protected ArrayList<TSocket_base> activeSockets;
  
  protected Log log;

  protected Protocol_base(SimNet net) {
    network = net;
    lk = new ReentrantLock();
    listenSockets = new ArrayList(); // <-- sockets listen (ServerSocket)
    activeSockets = new ArrayList(); // <-- sockets actius (Socket)
    log = Log.getLog();
    new Thread(new ReceiverTask()).start();
  }
  
  protected abstract void ipInput(TCPSegment segment);
  protected abstract TSocket_base getMatchingTSocket(int localPort, int remotePort);

  public SimNet getNetwork() {
    return network;
  }

  //-------------------------------------------

  /**
   * Add a TSock to list of listen TSocks. We assume the TSock is in state LISTEN.
   */
  public void addListenTSocket(TSocket_base sc) {
    lk.lock();
    listenSockets.add(sc);
    lk.unlock();
  }

  /**
   * Add a TSock to list of active TSocks. We assume the TSock is in an active state.
   */
  public void addActiveTSocket(TSocket_base sc) {
    lk.lock();
    activeSockets.add(sc);
    lk.unlock();
  }

  /**
   * Remove a TSock from list of listen TSocks. We assume the TSock is in CLOSED state.
   */
  public void removeListenTSocket(TSocket_base sc) {
    lk.lock();
    listenSockets.remove(sc);
    lk.unlock();
  }

  /**
   * Remove a TSock from list of active TSocks. We assume the TSock is in CLOSED state.
   */
  public void removeActiveTSocket(TSocket_base sc) {
    lk.lock();
    activeSockets.remove(sc);
    lk.unlock();
  }

  //-------------------------------------------
  
  class ReceiverTask implements Runnable {

    @Override
    public void run() {
      while (true) {
        TCPSegment rseg = network.receive();
        ipInput(rseg);
      }
    }
  }
}
