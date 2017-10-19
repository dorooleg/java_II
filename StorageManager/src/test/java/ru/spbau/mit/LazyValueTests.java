package ru.spbau.mit;

import org.junit.Test;
import ru.spbau.mit.util.CountingSupplier;
import ru.spbau.mit.util.InvocationCountingSupplier;

import static org.junit.Assert.*;

public class LazyValueTests extends AbstractLazyValueTest {

    @Test
    public void testTrivialGet() throws Throwable {
        doTest(new CountingSupplier<>(42), 42);
    }

    @Test
    public void testNullReturn() throws Throwable {
        doTest(new CountingSupplier<>(() -> null), null);
    }

    @Test
    public void testThrowingSupplier() throws Throwable {
        RuntimeException fail = new RuntimeException("Fail");
        InvocationCountingSupplier<Object> supplier = new CountingSupplier<>(() -> {
            throw fail;
        });
        LazyValue<Object> lazyValue = storageManager.createLazyValue(supplier);
        stressTestLazyValue(lazyValue, supplier, fail);
    }

}