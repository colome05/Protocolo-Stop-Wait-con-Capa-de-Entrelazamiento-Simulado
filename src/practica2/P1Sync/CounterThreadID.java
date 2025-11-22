package practica2.P1Sync;

public class CounterThreadID extends Thread {
    private final int id;

    public CounterThreadID(int id) {
        this.id = id;
    }
    
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.print(id);
        }
    }
}
