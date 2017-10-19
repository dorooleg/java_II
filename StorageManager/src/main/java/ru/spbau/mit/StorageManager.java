package ru.spbau.mit;

import java.util.function.Function;
import java.util.function.Supplier;

public interface StorageManager {
    <R> LazyValue<R> createLazyValue(Supplier<R> supplier);
    <T, R> MemoizedFunction<T, R> createMemoizedFunction(Function<T, R> function);

    /** Для задания №3 */
    <R> LazyValue<R> createLazyValue(Supplier<R> supplier, Supplier<R> onRecursion);
    <T, R> MemoizedFunction<T, R> createMemoizedFunction(Function<T, R> function, Function<T, R> onRecursion);
}
