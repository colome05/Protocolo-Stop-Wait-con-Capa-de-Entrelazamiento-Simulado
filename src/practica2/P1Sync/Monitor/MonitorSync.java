package practica2.P1Sync.Monitor;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorSync {

    private final int N;
    private int turno;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition cond = lock.newCondition();

    public MonitorSync(int N) {
        this.N = N;
    }

    public void waitForTurn(int id) {

        lock.lock();
        try {
            while (turno != id) {
                cond.awaitUninterruptibly();
            }
            System.out.print(id);
        } finally {
            lock.unlock();
        }

    }

    public void transferTurn() {
        lock.lock();
        try {
            turno = (turno + 1) % N;
            cond.signal();
        } finally {
            lock.unlock();
        }
    }
}
