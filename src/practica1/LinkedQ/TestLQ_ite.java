package practica1.LinkedQ;

import java.util.Iterator;

public class TestLQ_ite {

    public static void main(String[] args) {

        LinkedQueue<Integer> q = new LinkedQueue<>();
        for (int i = 0; i < 10; i++) {
            q.put(i);
        }
        System.out.println("Queue content: " + q);

        Iterator<Integer> ite = q.iterator();

        System.out.println("we iterate over the queue elements to take 0,1,4,5,8 and 9, if present:");
        while (ite.hasNext()) {
            int valor = ite.next();
            if (valor == 0 || valor == 1 || valor == 4 || valor == 5 || valor == 8 || valor == 9) {
                ite.remove();
                System.out.println("taken: " + valor);
            }
        }
        System.out.println("present content of the queue: " + q);
        for (int i = 10; i < 13; i++) {
            q.put(i);
        }
        System.out.println("Queue content: " + q);
        ite = q.iterator();

        System.out.println("we iterate over the queue and remove the elements:");
        while (ite.hasNext()) {
            int valor = ite.next();
            ite.remove();
            System.out.println("taken: " + valor);
        }
        System.out.println("present content of the queue: " + q);
    }
}
