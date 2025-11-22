package util;

public interface Const {

  //random parameter:
  int SEED              = 1;

  //simulated network parameters:
  int SIMNET_QUEUE_SIZE = 100;
  double LOSS_RATE_PSH  = 0.2; 
  double LOSS_RATE_ACK  = 0.2;

  //network parameters: MSS = 1500 - 20 - 20 = 1460 bytes
  int MTU_ETHERNET      = 1500;
  int IP_HEADER         = 20;
  int TCP_HEADER        = 20;

  //sender parameters:
  int SND_RTO           = 500;
  
  int SND_NUM           = 5;
  int SND_SIZE          = 3000;
  int SND_INTERVAL      = 100;
  
  //receiver parameters:
  int RCV_QUEUE_SIZE    = 50;
  
  int RCV_SIZE          = 2000;
  int RCV_INTERVAL      = 500;
  
  //server parameter:
  int LISTEN_QUEUE_SIZE = 10;

}
