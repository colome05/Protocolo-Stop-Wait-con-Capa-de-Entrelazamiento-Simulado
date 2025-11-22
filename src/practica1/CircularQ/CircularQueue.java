package practica1.CircularQ;

import java.util.Iterator;
import util.Queue;

public class CircularQueue<E> implements Queue<E> {

    private final E[] queue;
    private final int N;
    private int G, P, nElem = 0;

    public CircularQueue(int N) {
        this.N = N;
        queue = (E[]) (new Object[N]);
    }

    @Override
    public int size() {
        return nElem;
    }

    @Override
    public int free() {
        return N - nElem;
    }

    @Override
    public boolean empty() {
        return nElem == 0;
    }

    @Override
    public boolean full() {
        return nElem == N;
    }

    @Override
    public E peekFirst() {
        return queue[G];
    }

    @Override
    public E get() {
        E hola = queue[G];
        G = (G + 1) % N;
        nElem--;
        return hola;
    }

    @Override
    public void put(E e) {
        queue[P] = e;
        nElem++;
        P = (P + 1) % N;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < nElem; i++) {
            sb.append(queue[(G + i) % N]);
            if (i < nElem - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
    }

    class MyIterator implements Iterator {

        protected int retornats = 0;

        @Override
        public boolean hasNext() {
            return retornats < nElem;
        }

        @Override
        public E next() {
            E hola = queue[retornats];
            retornats = retornats + 1;
            return hola;
        }

        @Override
        public void remove() {
            for (int i = retornats; i < nElem; i++) {
                queue[(i - 1) % N] = queue[i % N];
            }
            retornats = retornats - 1;
            nElem = nElem - 1;
            P = (P - 1) % N;
        }

    }
}
