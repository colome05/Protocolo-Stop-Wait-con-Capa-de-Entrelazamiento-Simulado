package util;

public class Receiver extends Thread {

  protected TSocket_base input;
  protected int recvBuf, recvInterval;
  public static int numReceivers;
  private Log log;

  public Receiver(TSocket_base sc, int recvBuf, int recvInterval) {
    this.input        = sc;
    this.recvBuf      = recvBuf;
    this.recvInterval = recvInterval;
    numReceivers = numReceivers + 1;
    log = Log.getLog();
  }

  public Receiver(TSocket_base sc) {
    this(sc, Const.RCV_SIZE, Const.RCV_INTERVAL);
  }

  @Override
  public void run() {
    try {
      byte n = 0;
      int total = 0;
      byte[] buf = new byte[recvBuf];
      Thread.sleep(200);
      while (total < Sender.numBytes) {
        int r = input.receiveData(buf, 0, buf.length);
        total = total + r;
        // check received data stamps
        for (int j = 0; j < r; j++) {
          if (buf[j] != n) {
            log.printRED("\t\t\t\t\t\t\t\tReceiver: RECEIVED DATA IS CORRUPTED!!!");
            System.exit(0);
          }
          n = (byte) (n + 1);
        }
        log.printBLUE("\t\t\t\t\t\t\t\tReceiver: received " + r + " bytes");
        Thread.sleep(recvInterval);
      }
      log.printGREEN("\t\t\t\t\t\t\t\tReceiver: reception finished");
      numReceivers = numReceivers - 1;
      if (Sender.numSenders == 0 && Receiver.numReceivers == 0) {
        Thread.sleep(1000);
        System.exit(0);
      }
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

}
