package util;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TSocket_base {

    public SimNet network;

    protected Lock lock;
    protected Condition appCV;

    public int localPort;
    public int remotePort;

    protected Timer timerService;

    protected Log log;

    protected TSocket_base(SimNet network) {
        this.network = network;
        lock = new ReentrantLock();
        appCV = lock.newCondition();
        timerService = new Timer();
        log = Log.getLog();
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public void listen() {
        throw new RuntimeException("Not supported yet.");
    }

    public TSocket_base accept() {
        throw new RuntimeException("Not supported yet.");
    }

    public void connect() {
        throw new RuntimeException("Not supported yet.");
    }

    public void close() {
        throw new RuntimeException("Not supported yet.");
    }

    public void sendData(byte[] data, int offset, int length) {
        throw new RuntimeException("Not supported yet.");
    }

    public int receiveData(byte[] data, int offset, int length) {
        throw new RuntimeException("Not supported yet.");

    }

    public void processReceivedSegment(TCPSegment rseg) {
        throw new RuntimeException("Not supported yet.");
    }

    protected void timeout(TCPSegment seg) {
        throw new RuntimeException("Not supported yet.");
    }

    protected void startRTO(TCPSegment seg) {
        TimerTask sndRtTimer = new TimerTask() {
            @Override
            public void run() {
                timeout(seg);
            }
        };
        timerService.schedule(sndRtTimer, Const.SND_RTO);
    }

    protected void printRcvSeg(TCPSegment rseg) {
        if (rseg.isPsh()) {
            log.printBLACK("\t\t\t\t\t\t\t\treceived: " + rseg);
        }
        if (rseg.isAck()) {
            log.printBLACK("  received: " + rseg);
        }
    }

    protected void printSndSeg(TCPSegment rseg) {
        if (rseg.isPsh()) {
            log.printBLACK("  sent: " + rseg);
        }
        if (rseg.isAck()) {
            log.printBLACK("\t\t\t\t\t\t\t\tsent: " + rseg);
        }
    }

}
