package practica2.P0CZ;

public class TestSum {

    public static void main(String[] args) throws InterruptedException {
        CounterThread ct1 = new CounterThread();
        CounterThread ct2 = new CounterThread();
        
        ct1.start();
        ct2.start();
        ct1.join(); // espera a que ct1 acabi
        
        System.out.print(ct1.x);
        
    }
}
