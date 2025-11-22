package practica2.P1Sync.Monitor;

public class TestIDSync {

    public static void main(String[] args) {
        int N = 2;
        MonitorSync mon = new MonitorSync(N);

        CounterThreadIDSync[] f = new CounterThreadIDSync[N];
        for (int i = 0; i < N; i++) {
            f[i] = new CounterThreadIDSync(mon, i);
            f[i].start();
        }
        for (int i = 0; i < N; i++) {
            try {
                f[i].join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("\nSimulation end.");
    }
}
