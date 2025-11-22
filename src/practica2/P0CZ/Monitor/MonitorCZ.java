package practica2.P0CZ.Monitor;

import static java.lang.Thread.sleep;
import java.util.concurrent.locks.ReentrantLock;
import static practica2.P0CZ.CounterThread.x;

public class MonitorCZ {

    private int x = 0;
    private ReentrantLock lock;

    public void inc() {
        lock.lock();
        x = x + 1;
        lock.unlock();
    }

    public int getX() {
        return x;
    }
    

}
