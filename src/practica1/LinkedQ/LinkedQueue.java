package practica1.LinkedQ;

import java.util.Iterator;
import util.Queue;

public class LinkedQueue<E> implements Queue<E> {

    protected int nElem;
    protected Node<E> primer, ultim = new Node<>();

    @Override
    public int size() {
        return nElem;
    }

    @Override
    public int free() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean empty() {
        return nElem == 0;
    }

    @Override
    public boolean full() {
        return false;
    }

    @Override
    public E peekFirst() {
        if (this.empty()) {
            throw new RuntimeException("Cola está vacía");
        }
        return primer.getValue();
    }

    @Override
    public E get() {
        if (nElem == 0) {
            throw new IllegalStateException("Cua buida");
        } else {
            E value = primer.getValue();
            primer = primer.getNext();
            nElem--;
            return value;
        }
    }

    @Override
    public void put(E e) {
        Node<E> aux = new Node();
        aux.setValue(e);
        if (!empty()) {
            ultim.setNext(aux);
            ultim = aux;
        } else {
            primer = ultim = aux;
        }
        nElem++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Queue content: ");
        if (!this.empty()) {
            Node<E> aqui = primer;
            while (aqui.getNext() != null) {
                sb.append(aqui.getValue()).append(" ");
                aqui = aqui.getNext();
            }
            sb.append(ultim.getValue()).append(" ");
        }
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new MyIterator();
    }

    class MyIterator implements Iterator {

        Node<E> it = primer;
        Node<E> pos1;
        Node<E> pos2;

        @Override
        public boolean hasNext() {
            return it.getNext() != null;
        }

        @Override
        public E next() {
            pos1 = pos2;
            pos2 = it;
            it = it.getNext();
            return pos2.getValue();
        }

        @Override
        public void remove() {
            if (pos2 == primer) {
                primer = it;
            } else {
                pos1.setNext(it);
            }
            nElem--;
        }
    }

}

