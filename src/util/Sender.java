package util;

public class Sender extends Thread {

  protected TSocket_base output;
  protected int sendNum, sendSize, sendInterval;
  public static int numBytes, numSenders;
  private Log log;

  public Sender(TSocket_base sc, int sendNum, int sendSize, int sendInterval) {
    this.output       = sc;
    this.sendNum      = sendNum;
    this.sendSize     = sendSize;
    this.sendInterval = sendInterval;
    numBytes = sendNum * sendSize;
    numSenders = numSenders + 1;
    log = Log.getLog();
  }

  public Sender(TSocket_base sc) {
    this(sc, Const.SND_NUM, Const.SND_SIZE, Const.SND_INTERVAL);
  }

  public void run() {
    try {
      byte n = 0;
      byte[] buf = new byte[sendSize];
      for (int i = 0; i < sendNum; i++) {
        Thread.sleep(sendInterval);
        // stamp data to send
        for (int j = 0; j < sendSize; j++) {
          buf[j] = n;
          n = (byte) (n + 1);
        }
        output.sendData(buf, 0, buf.length);
      }
      log.printGREEN("Sender: transmission finished");
      numSenders = numSenders - 1;
    } catch (Exception e) {
      log.printRED("Excepcio a Sender: ");
      e.printStackTrace(System.err);
    }
  }

}
