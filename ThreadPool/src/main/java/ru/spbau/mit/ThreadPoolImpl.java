package ru.spbau.mit;

import java.util.*;
import java.util.function.Supplier;

public class ThreadPoolImpl implements ThreadPool {

    private final List<Thread> threads;
    private final LinkedHashSet<FutureTask> tasks;

    ThreadPoolImpl(final int size) {
        threads = new ArrayList<>(size);
        tasks = new LinkedHashSet<>();
        for (int i = 0; i < size; i++) {
            Thread thread = new Thread(new ThreadHandler());
            threads.add(thread);
            thread.start();
        }
    }

    public <T> LightFuture<T> add(final Supplier<T> task) {
        Objects.requireNonNull(task);
        final FutureTask<T> future = new FutureTask<>(task, this);
        synchronized (tasks) {
            tasks.add(future);
            tasks.notifyAll();
        }
        return future;
    }

    public void add(final FutureTask futureTask) {
        Objects.requireNonNull(futureTask);
        synchronized (tasks) {
            tasks.add(futureTask);
            tasks.notifyAll();
        }
        synchronized (futureTask) {
            futureTask.notifyAll();
        }
    }

    FutureTask tryPop(FutureTask task) {
        synchronized (tasks) {
            if (tasks.remove(task)) {
                return task;
            }
            return null;
        }
    }

    private FutureTask tryPop() {
        synchronized (tasks) {
            if (tasks.isEmpty()) {
                return null;
            }
            FutureTask task = tasks.iterator().next();
            tasks.remove(task);
            return task;
        }
    }

    public void shutdown() {
        synchronized (tasks) {
            for (FutureTask task : tasks) {
                task.stop();
                synchronized (task) {
                    task.notifyAll();
                }
            }
            tasks.clear();
        }

        synchronized (threads) {
            for (Thread thread : threads) {
                while (thread.getState() != Thread.State.TERMINATED
                        && thread.getState() != Thread.State.NEW) {
                    thread.interrupt();
                }
                try {
                    thread.join();
                } catch (InterruptedException ignored) {
                }
            }

            threads.clear();
        }
    }

    private class ThreadHandler implements Runnable {

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                FutureTask task = tryPop();
                if (task == null) {
                    synchronized (tasks) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                } else {
                    task.run();
                }
            }
        }
    }
}
