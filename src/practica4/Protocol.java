package practica4;

import util.Protocol_base;
import util.TCPSegment;
import util.SimNet;
import util.TSocket_base;

public class Protocol extends Protocol_base {

    public Protocol(SimNet network) {
      super(network);
    }

    protected void ipInput(TCPSegment seg) {
        
        TSocket_base TS = getMatchingTSocket(seg.getSourcePort(), seg.getDestinationPort());
        if (TS != null) {
            TS.processReceivedSegment(seg);
        }
        
    }

    protected TSocket_base getMatchingTSocket(int localPort, int remotePort) {
        lk.lock();
        try {
            
            for(TSocket_base TS : activeSockets) {
                
                if(TS.getLocalPort() == remotePort && TS.getRemotePort() == localPort) {
                    return TS;
                }            
            
            }
            
            return null;
            
        } finally {
            lk.unlock();
        }
    }
}
