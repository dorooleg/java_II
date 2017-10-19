package ru.spbau.mit;

import org.junit.Test;

import static org.junit.Assert.*;
import ru.spbau.mit.util.ApplicativeConverter;
import ru.spbau.mit.util.InvocationCountingFunction;
import ru.spbau.mit.util.InvocationCountingSupplier;

public class RecursiveLazyTest extends AbstractLazyValueTest {

    @Test(expected = RecursiveComputationException.class)
    public void get() throws RecursiveComputationException, InterruptedException {
        LazyValue<Integer> value = storageManager.createLazyValue(() -> {
            LazyValue<Integer> inner = storageManager.createLazyValue(() -> 5);
            try {
                return inner.get();
            } catch (Exception ignore) {
                return null;
            }
        });

        value.get();
    }

}