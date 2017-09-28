package ru.spbau.mit;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class FutureTask<T> implements LightFuture<T> {

    // Must be atomic. incrementAndGet
    private static long globalId;
    private final Supplier<T> task;
    private final long id;
    private volatile T result;
    private volatile LightExecutionException exception;
    private volatile boolean isReady;
    private final ThreadPoolImpl threadPool;

    FutureTask(final Supplier<T> task, final ThreadPoolImpl threadPool) {
        Objects.requireNonNull(task);
        Objects.requireNonNull(threadPool);
        this.task = task;
        this.threadPool = threadPool;
        synchronized (FutureTask.class) {
            id = globalId++;
        }
    }

    long getId() {
        return id;
    }

    void stop() {
        exception = new LightExecutionException();
    }


    @Override
    public boolean isReady() {
        return isReady;
    }

    @Override
    public synchronized T get() throws LightExecutionException {

        while (!isReady && exception == null) {

            final FutureTask task = threadPool.tryPop(id);

            if (task != null) {
                run();
                break;
            }

            try {
                synchronized (this) {
                    wait();
                }
            } catch (InterruptedException e) {
                throw new LightExecutionException();
            }
        }

        if (exception != null) {
            throw exception;
        }

        return result;
    }

    @Override
    public <U> LightFuture<U> thenApply(Function<? super T, ? extends U> fn) {
        Objects.requireNonNull(fn);
        return threadPool.add(() -> {
            try {
                return fn.apply(FutureTask.this.get());
            } catch (LightExecutionException ignored) {
            }
            return null;
        });
    }

    void run() {

        result = task.get();
        isReady = true;

        synchronized (this) {
            notify();
        }
    }
}
