package practica4;

import util.SimNet_FullDuplex;
import util.Receiver;
import util.Sender;
import util.SimNet;

public class Test {

  public static void main(String[] args) {

    SimNet_FullDuplex net = new SimNet_FullDuplex();

    new Thread(new HostRcv(net.getRcvEnd())).start();
    new Thread(new HostSnd(net.getSndEnd())).start();
  }
}

class HostRcv implements Runnable {

  public static final int PORT = 80;

  protected Protocol proto;

  public HostRcv(SimNet net) {
    this.proto = new Protocol(net);
  }

  public void run() {
    //arranca dos fils receptors, cadascun amb el seu socket de recepcio
    //fes servir els ports apropiats
    new Receiver(new TSocket(proto, HostRcv.PORT, HostSnd.PORT1), 2000, 50).start();
    new Receiver(new TSocket(proto, HostRcv.PORT, HostSnd.PORT2), 2000, 10).start();
  }
}

class HostSnd implements Runnable {

  public static final int PORT1 = 20;
  public static final int PORT2 = 30;

  protected Protocol proto;

  public HostSnd(SimNet c) {
    this.proto = new Protocol(c);
  }

  public void run() {
    //arranca dos fils emissors, cadascun amb el seu socket de transmissio
    //fes servir els ports apropiats
    new Sender(new TSocket(proto, HostSnd.PORT1, HostRcv.PORT)).start();
    new Sender(new TSocket(proto, HostSnd.PORT2, HostRcv.PORT)).start();
  }
}
