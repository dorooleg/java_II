package mit.spbau.ru;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockFreeList<T extends Comparable<T>> implements List<T> {

    private AtomicMarkableReference<Node> head = new AtomicMarkableReference<>(new Node(), false);

    @Override
    public int size() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        boolean[] mark = new boolean[1];
        return head.getReference().next.get(mark) == null || mark[0];
    }

    @Override
    public boolean contains(Object o) {
        final Triple triple = find((T) o);
        return triple.curr != null;
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        Node newNode = new Node((T) t);

        while (true) {
            final Triple triple = find((T) t);

            if (triple.curr != null) {
                return false;
            }

            newNode.next = new AtomicMarkableReference<>(triple.succ, false);

            if (triple.pred.next.compareAndSet(triple.succ, newNode, false, false))
                return true;
        }
    }

    @Override
    public boolean remove(Object o) {
        while (true) {
            final Triple triple = find((T) o);

            if (triple.curr == null) {
                return false;
            }

            if (!triple.curr.next.attemptMark(triple.succ, true)) {
                continue;
            }

            triple.pred.next.compareAndSet(triple.curr, triple.succ, false, false);

            return true;
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T set(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    private Triple find(T value) {
        Node pred, curr, succ = null;
        boolean[] cmark = new boolean[1];
        retry:
        while (true) {
            pred = head.getReference();
            while (true) {
                curr = pred.next.getReference();
                if (curr == null) {
                    return new Triple(pred, null, succ);
                }

                succ = curr.next.get(cmark);

                if (!cmark[0]) {
                    if (curr.value == value) {
                        return new Triple(pred, curr, succ);
                    } else if (curr.value.compareTo(value) <= 0) {
                        pred = curr;
                    } else {
                        return new Triple(pred, null, curr);
                    }
                } else if (!pred.next.compareAndSet(curr, succ, false, false)) {
                    continue retry;
                }
            }
        }
    }

    private class Triple {
        Node pred;
        Node curr;
        Node succ;

        Triple(Node pred, Node curr, Node succ) {
            this.pred = pred;
            this.curr = curr;
            this.succ = succ;
        }
    }

    private class Node {

        T value;
        AtomicMarkableReference<Node> next;

        Node(T value) {
            this.value = value;
            next = new AtomicMarkableReference<>(null, false);
        }

        Node() {
            next = new AtomicMarkableReference<>(null, false);
        }
    }
}
