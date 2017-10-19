package ru.spbau.mit;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConcurrentMemorizeFunction<T, R> implements MemoizedFunction<T, R> {

    private final Function<T, R> function;
    private final Function<T, R> onRecursion;
    private final HashMap<T, LazyValue<R>> cachedValue = new HashMap<>();

    public ConcurrentMemorizeFunction(Function<T, R> function) {
        this.function = function;
        this.onRecursion = null;
    }

    public ConcurrentMemorizeFunction(Function<T, R> function, Function<T, R> onRecursion) {
        this.function = function;
        this.onRecursion = onRecursion;
    }

    @Override
    public R apply(T argument) throws RecursiveComputationException, InterruptedException {
        return getLazy(argument).get();
    }

    private LazyValue<R> getLazy(T argument) {
        LazyValue<R> result;
        synchronized (cachedValue) {
            if (onRecursion != null) {
                result = cachedValue.computeIfAbsent(argument, a -> new ConcurrentLazy<>(() -> function.apply(a)));
            } else {
                result = cachedValue.computeIfAbsent(argument, a -> new ConcurrentLazy<>(() -> function.apply(a), () -> onRecursion.apply(a)));
            }
        }
        return result;
    }

    @Override
    public boolean isComputedAt(T argument) {
        return getLazy(argument).isReady();
    }
}
