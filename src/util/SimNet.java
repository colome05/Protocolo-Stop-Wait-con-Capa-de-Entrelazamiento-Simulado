package util;

public interface SimNet {

  void       send(TCPSegment seg);
  TCPSegment receive();
  int        getMTU();  // MTU: Maximum Transmission Unit (Link Layer)
  
}
