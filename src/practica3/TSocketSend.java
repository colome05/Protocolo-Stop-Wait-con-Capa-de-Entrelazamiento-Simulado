package practica3;

import util.Const;
import util.TCPSegment;
import util.TSocket_base;
import util.SimNet;

public class TSocketSend extends TSocket_base {

    protected int MSS;       // Maximum Segment Size

    public TSocketSend(SimNet network) {
        super(network);
        MSS = network.getMTU() - Const.IP_HEADER - Const.TCP_HEADER;
    }

    @Override
    public void sendData(byte[] data, int offset, int length) {
        int bytesPerEnviar = length;
        int numBytes = 0;

        while (bytesPerEnviar > 0) {

            numBytes = Math.min(MSS, bytesPerEnviar);

            TCPSegment seg = segmentize(data, offset, numBytes);
            network.send(seg);
            printSndSeg(seg);

            offset = offset + numBytes;
            bytesPerEnviar = bytesPerEnviar - numBytes;
        }
    }

    protected TCPSegment segmentize(byte[] data, int offset, int length) {
        TCPSegment fragment = new TCPSegment();
        fragment.setData(data, offset, length);
        fragment.setPsh(true);
        return fragment;
    }

}
