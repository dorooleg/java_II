package ru.spbau.mit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class FutureTask<T> implements LightFuture<T> {
    private final Supplier<T> task;
    private final ThreadPoolImpl threadPool;
    private final List<FutureTask> dependencies = new ArrayList<>();
    private volatile T result;
    private volatile LightExecutionException exception;
    private volatile boolean isReady;

    FutureTask(final Supplier<T> task, final ThreadPoolImpl threadPool) {
        Objects.requireNonNull(task);
        Objects.requireNonNull(threadPool);
        this.task = task;
        this.threadPool = threadPool;
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
            if (Thread.currentThread().isInterrupted()) {
                exception = new LightExecutionException();
                break;
            }

            final FutureTask task = threadPool.tryPop(this);

            if (task != null) {
                run();
                break;
            }

            try {
                synchronized (this) {
                    wait();
                }
            } catch (InterruptedException e) {
                throw new LightExecutionException(e);
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
        synchronized (dependencies) {
            Supplier<U> supplier = () -> {
                try {
                    return fn.apply(get());
                } catch (LightExecutionException ignored) {
                }
                return null;
            };

            if (isReady()) {
                return threadPool.add(supplier);
            }

            FutureTask<U> task = new FutureTask<>(supplier, threadPool);
            dependencies.add(task);
            return task;
        }
    }

    void run() {

        try {
            result = task.get();
        } catch (Exception e) {
            exception = new LightExecutionException(e);
        }

        synchronized (dependencies) {
            isReady = true;
            for (FutureTask element : dependencies) {
                threadPool.add(element);
            }
            dependencies.clear();
        }

        synchronized (this) {
            notifyAll();
        }
    }
}
