package practica1.Protocol;

import util.TCPSegment;
import util.TSocket_base;
import util.SimNet;

public class TSocketRecv extends TSocket_base {

    public TSocketRecv(SimNet network) {
        super(network);
    }

    @Override
    public int receiveData(byte[] data, int offset, int length) {
        TCPSegment seg = network.receive();

        if (seg == null) {
            log.printRED("Paquet buit");
            return 0;
        } else {

            byte[] rebuts = seg.getData();

            int nElem = 0;
            
            System.arraycopy(rebuts, 0, data, offset, Math.min(seg.getDataLength(), length));

//            for (int i = 0; i < Math.min(seg.getDataLength(), length); i++) {
//
//                data[i + offset] = rebuts[i];
//                nElem++;
//
//            }

            printRcvSeg(seg);
            return Math.min(seg.getDataLength(), length);
        }
    }
}
