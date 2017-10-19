package ru.spbau.mit;

public interface LazyValue<R> {
    /** RecursiveComputationException для задания №3 */
    R get() throws RecursiveComputationException, InterruptedException;

    boolean isReady();
}
