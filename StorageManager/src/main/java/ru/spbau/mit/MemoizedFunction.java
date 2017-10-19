package ru.spbau.mit;

public interface MemoizedFunction<T, R> {
    /** RecursiveComputationException для задания №3 */
    R apply(T argument) throws RecursiveComputationException, InterruptedException;

    boolean isComputedAt(T argument);
}
