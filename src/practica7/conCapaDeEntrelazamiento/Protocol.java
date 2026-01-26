package practica7.conCapaDeEntrelazamiento; //ACABADA

import practica7.*;
import util.Protocol_base;
import util.TCPSegment;
import util.SimNet;
import util.TSocket_base;

public class Protocol extends Protocol_base {

    protected Protocol(SimNet network) {
        super(network);
    }

    public void ipInput(TCPSegment segment) {
        TSocket_base TS = getMatchingTSocket(segment.getSourcePort(), segment.getDestinationPort());
        if (TS != null) {
            TS.processReceivedSegment(segment);
        }
        //else
            //System.out.println(TS);
    }

    protected TSocket_base getMatchingTSocket(int localPort, int remotePort) {
        lk.lock();
        try {
            for (TSocket_base tsb : activeSockets) {
                //System.out.println("ACTIVE: LocalPortTSB: " + tsb.localPort + " RemotePort: " + remotePort);
                if (tsb.getLocalPort() == remotePort && tsb.getRemotePort() == localPort) {
                    return tsb;
                }
            }
            for (TSocket_base tsb : listenSockets) { //NOMES CAL COMPROVAR EL LOCAL AMB EL LOCAL (80 ELS DOS)
                //System.out.println("LISTEN: LocalPortTSB: " + tsb.localPort + " RemotePort: " + remotePort);
                if (tsb.getLocalPort() == remotePort) { //abans era localPort
                    return tsb;
                }
            }
            return null;
        } finally {
            lk.unlock();
        }
    }

}
