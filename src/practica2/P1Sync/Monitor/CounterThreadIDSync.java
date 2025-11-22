package practica2.P1Sync.Monitor;

public class CounterThreadIDSync extends Thread {

    private final MonitorSync mon;
    private final int id;

    public CounterThreadIDSync(MonitorSync mon, int id) {
        this.mon = mon;
        this.id = id;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            mon.waitForTurn(id);
            mon.transferTurn();
        }
    }

}
