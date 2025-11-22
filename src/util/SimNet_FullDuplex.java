package util;

import practica3.SimNet_Loss;

public class SimNet_FullDuplex {

  protected SimNet_Loss instance_left, instance_right;
  protected Peer left, right;

  public SimNet_FullDuplex(double lossPsh, double lossAck) {
    instance_left  = new SimNet_Loss(lossAck);
    instance_right = new SimNet_Loss(lossPsh);
    left  = new Peer();
    right = new Peer();
  }

  public SimNet_FullDuplex() {
    this(0.0, 0.0);
  }

  public SimNet getSndEnd() {
    return left;
  }
  
  public SimNet getRcvEnd() {
    return right;
  }
  
  public SimNet getCltEnd() {
    return left;
  }
  
  public SimNet getSrvEnd() {
    return right;
  }

  public class Peer implements SimNet {

    @Override
    public void send(TCPSegment seg) {
      if (this == left) {
        instance_right.send(seg);
      } else {
        instance_left.send(seg);
      }
    }

    @Override
    public TCPSegment receive() {
      if (this == left) {
        return instance_left.receive();
      } else {
        return instance_right.receive();
      }
    }

    @Override
    public int getMTU() {
      return Const.MTU_ETHERNET;
    }

  }

}
