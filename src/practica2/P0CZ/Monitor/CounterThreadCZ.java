package practica2.P0CZ.Monitor;

public class CounterThreadCZ extends Thread {

    private final MonitorCZ mon;
    private final int numberOfIterations = 10000;

    public CounterThreadCZ(MonitorCZ monitor) {
        this.mon = monitor;
    }

    @Override
    public void run() {
        for (int i = 0; i < numberOfIterations; i++) {
            mon.inc();
        }
    }
}
