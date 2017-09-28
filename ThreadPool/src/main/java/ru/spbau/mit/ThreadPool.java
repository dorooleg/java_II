package ru.spbau.mit;

import java.util.function.Supplier;

public interface ThreadPool {

    void shutdown();

    <T> LightFuture<T> add(final Supplier<T> task);
}
