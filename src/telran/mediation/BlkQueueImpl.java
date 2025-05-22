package telran.mediation;

import java.util.LinkedList;

public class BlkQueueImpl<T> implements BlkQueue<T> {
    private final LinkedList<T> queue;
    private final int maxSize;

    public BlkQueueImpl(int maxSize) {
        this.maxSize = maxSize;
        queue = new LinkedList<>();
    }

    @Override
    public synchronized void push(T message) {

        while (queue.size() >= maxSize) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("thread was interrupted");
            }
        }
        queue.add(message);
        notifyAll();
    }


    @Override
    public synchronized T pop() {
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("thread was interrupted");
            }
        }
        T msg = queue.poll();
        notifyAll();
        return msg;
    }
}
