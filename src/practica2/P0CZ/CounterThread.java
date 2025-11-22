package practica2.P0CZ;

public class CounterThread extends Thread {

    public static int x;
    private final int I = 100;

    @Override
    public void run() {
        
        int R;
        
        for (int i = 0; i < I; i++) {
            R = x;
            try {
            
                sleep(1);
            
            }catch (InterruptedException ex ){
            
            }
            
            R = R + 1;
            x = R;
        }
    }
}
