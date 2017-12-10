package mit.spbau.ru;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockFreeList<T> implements ILockFreeList<T> {

    private AtomicMarkableReference<Node> head = new AtomicMarkableReference<>(new Node(), false);

    @Override
    public boolean isEmpty() {
        final boolean[] mark = new boolean[1];

        while (true) {
            Node curr = head.getReference().next.getReference();
            if (curr == null) {
                return true;
            }

            if (curr.next.get(mark) != null && !mark[0]) {
                return false;
            }

            head.getReference().next.compareAndSet(curr, curr.next.getReference(), false, false);
        }
    }

    @Override
    public void append(T value) {
        Node newNode = new Node(value);

        while (true) {
            final Triple triple = find(value);

            if (triple.curr != null) {
                return;
            }

            newNode.next = new AtomicMarkableReference<>(triple.succ, false);

            if (triple.pred.next.compareAndSet(triple.succ, newNode, false, false))
                return;
        }
    }

    @Override
    public boolean contains(T value) {
        final Triple triple = find(value);
        return triple.curr != null;
    }

    @Override
    public boolean remove(T value) {
        while (true) {
            final Triple triple = find(value);

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

    private Triple find(T value) {
        Node pred, curr, succ = null;
        boolean[] cmark = new boolean[1];
        boolean[] pmark = new boolean[1];
        retry:
        while (true) {
            pred = head.getReference();
            while (true) {
                curr = pred.next.get(pmark);
                if (curr == null) {
                    return new Triple(pred, null, succ);
                }

                succ = curr.next.get(cmark);

                if (pred.next.getReference() != curr || pred.next.isMarked() != pmark[0]) {
                    continue retry;
                }

                if (!cmark[0]) {
                    if (curr.value.equals(value)) {
                        return new Triple(pred, curr, succ);
                    } else {
                        pred = curr;
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
