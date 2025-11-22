package util;

public class TCPSegment {

  private boolean syn, psh, ack, fin;
  private int sourcePort, destinationPort;
  private int seqNum, ackNum, sackNum = -1; // sack = selective-ack
  private int wnd;
  private byte[] data;
  
  public static boolean SHOW_DATA = false;
  
  public void setSyn(boolean syn) {
    this.syn = syn;
  }
  
  public boolean isSyn() {
    return syn;
  }
  
  public void setPsh(boolean psh) {
    this.psh = psh;
  }
  
  public boolean isPsh() {
    return psh;
  }
  
  public void setAck(boolean ack) {
    this.ack = ack;
  }

  public boolean isAck() {
    return ack;
  }
  
  public void setFin(boolean fin) {
    this.fin = fin;
  }
  
  public boolean isFin() {
    return fin;
  }
  
  public void setSourcePort(int sourcePort) {
    this.sourcePort = sourcePort;
  }

  public int getSourcePort() {
    return sourcePort;
  }

  public void setDestinationPort(int destinationPort) {
    this.destinationPort = destinationPort;
  }

  public int getDestinationPort() {
    return destinationPort;
  }
  
  public void setSeqNum(int seqNum) {
    this.seqNum = seqNum;
  }

  public int getSeqNum() {
    return seqNum;
  }

  public void setAckNum(int ackNum) {
    this.ackNum = ackNum;
  }

  public int getAckNum() {
    return ackNum;
  }

  public void setSackNum(int ackNum) {
    this.sackNum = ackNum;
  }

  public int getSackNum() {
    return sackNum;
  }
  
  public void setWnd(int wnd) {
    this.wnd = wnd;
  }
  
  public int getWnd() {
    return wnd;
  }

  public void setData(byte[] d) {
    data = new byte[d.length];
    System.arraycopy(d, 0, data, 0, d.length);
  }

  public void setData(byte[] d, int offset, int len) {
    data = new byte[len];
    System.arraycopy(d, offset, data, 0, len);
  }
  
  public byte[] getData() {
    return data;
  }

  public int getDataLength() {
    if (data == null) {
      return 0;
    }
    return data.length;
  }

  @Override
  public String toString() {
    String str = "[";
    
    if(syn){
      str = str + "SYN" + 
                  ", src = " + sourcePort + 
                  ", dst = " + destinationPort +
                  ", seqNum = " + seqNum;
    }
    else if(fin){
      str = str + "FIN" + 
                  ", src = " + sourcePort + 
                  ", dst = " + destinationPort +
                  ", seqNum = " + seqNum;
    }
    else if(psh){
      str = str + "PSH" + 
                  ", src = " + sourcePort + 
                  ", dst = " + destinationPort +
                  ", seqNum = " + seqNum;
      if (data != null && SHOW_DATA) {
        str = str + ", data = {";
        for (int i = 0; i < data.length - 1; i++) {
          str = str + data[i] + ",";
        }
        str = str + data[data.length - 1] + "}";
      }
      else if(data != null){
          str = str + ", payload = "+ data.length;
      }
    }
    else if(ack){
      str = str + "ACK" + 
                  ", src = " + sourcePort + 
                  ", dst = " + destinationPort +
                  ", ackNum = " + ackNum + 
                  ", wnd = " + wnd;
      if (sackNum != -1) {
        str = str + ", sackNum = " + sackNum;
      }
    }
    
    return str+"]";
  }

}
