package ru.spbau.mit.util;

import java.util.function.Supplier;

public interface InvocationCountingSupplier<R> extends Supplier<R> {
    int getInvocationsCount();

    InvocationCountingSupplier<R> copy();
}
