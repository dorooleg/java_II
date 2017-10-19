package ru.spbau.mit;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.function.Supplier;

public class ConcurrentLazy<T> implements LazyValue<T> {
    private Supplier<T> supplier;
    private Supplier<T> onRecursion;
    private T cachedValue;
    Exception exception;
    private static final ThreadLocal<Boolean> callBegin = ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<ConcurrentLazy> prevLazy = new ThreadLocal<ConcurrentLazy>();


    public ConcurrentLazy(@NotNull Supplier<T> supplier) {
        this.supplier = supplier;
        this.onRecursion = null;
    }

    public ConcurrentLazy(@NotNull Supplier<T> supplier, @NotNull Supplier<T> onRecursion) {
        this.supplier = supplier;
        this.onRecursion = onRecursion;
    }

    @Override
    @Nullable
    public synchronized T get() throws RecursiveComputationException, InterruptedException {
        if (supplier != null) {
            if (callBegin.get()) {
                RecursiveComputationException recursiveComputationException = new RecursiveComputationException();
                prevLazy.get().exception = recursiveComputationException;
                throw recursiveComputationException;
            }

            try {
                callBegin.set(true);
                prevLazy.set(this);
                cachedValue = supplier.get();
            } catch (Exception e) {
                exception = e;
                prevLazy.set(null);
            } finally {
                callBegin.set(false);
            }
            supplier = null;
        }

        if (exception != null) {
            if (exception instanceof RuntimeException) {
                throw (RuntimeException)exception;
            }

            if (exception instanceof InterruptedException) {
                throw (InterruptedException) exception;
            }

            if (exception instanceof RecursiveComputationException) {
                if (onRecursion != null) {
                    supplier = onRecursion;
                    onRecursion = null;
                    get();
                }
                throw (RecursiveComputationException) exception;
            }
        }
        return cachedValue;
    }

    @Override
    public boolean isReady() {
        return supplier == null;
    }
}