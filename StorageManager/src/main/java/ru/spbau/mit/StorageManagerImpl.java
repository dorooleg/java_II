package ru.spbau.mit;

import java.util.function.Function;
import java.util.function.Supplier;

public class StorageManagerImpl implements StorageManager {
    @Override
    public <R> LazyValue<R> createLazyValue(Supplier<R> supplier) {
        return new ConcurrentLazy<>(supplier);
    }

    @Override
    public <T, R> MemoizedFunction<T, R> createMemoizedFunction(Function<T, R> function) {
        return new ConcurrentMemorizeFunction<>(function);
    }

    @Override
    public <R> LazyValue<R> createLazyValue(Supplier<R> supplier, Supplier<R> onRecursion) {
        return new ConcurrentLazy<>(supplier, onRecursion);
    }

    @Override
    public <T, R> MemoizedFunction<T, R> createMemoizedFunction(Function<T, R> function, Function<T, R> onRecursion) {
        return new ConcurrentMemorizeFunction<>(function, onRecursion);
    }
}
