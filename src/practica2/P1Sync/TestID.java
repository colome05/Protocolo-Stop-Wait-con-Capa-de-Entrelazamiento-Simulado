package practica2.P1Sync;

public class TestID {

    public static void main(String[] args) {
        int N = 2;
        CounterThreadID[] f = new CounterThreadID[N];
        for (int i = 0; i < N; i++) {
            f[i] = new CounterThreadID(i);
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
