package ru.spbau.mit;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Supplier;

public class RecursiveFibonacci {

    private final ThreadPoolImpl pool = new ThreadPoolImpl(2);

    @Test
    public void recursiveFibonacci() throws LightExecutionException {
        Assert.assertEquals(new Long(6765), pool.add(new Fib(20)).get());
        pool.shutdown();
    }

    private class Fib implements Supplier<Long> {
        long n;

        Fib(long n) {
            this.n = n;
        }

        @Override
        public Long get() {
            try {
                return n <= 1 ? n : pool.add(new Fib(n - 1)).get() + pool.add(new Fib(n - 2)).get();
            } catch (LightExecutionException e) {
                return 0L;
            }
        }
    }

}