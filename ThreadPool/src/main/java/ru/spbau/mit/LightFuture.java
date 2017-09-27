package ru.spbau.mit;

import java.util.function.Function;

public interface LightFuture<T> {

    boolean isReady();

    T get() throws LightExecutionException;

    <U> LightFuture<U> thenApply(Function<? super T, ? extends U> fn);
}
