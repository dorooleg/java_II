package ru.spbau.mit;

import org.junit.Test;

public class ThreadPoolImplTest {
    private volatile int counter;

    @Test(timeout=1000)
    public void add() throws Exception {
        ThreadPoolImpl pool = new ThreadPoolImpl(2);
        for (int i = 0; i < 100; i++) {
            pool.add(() -> {
                counter++;
                return null;
            });
        }
        while (counter != 100);
        pool.shutdown();
    }

    @Test
    public void shutdown() throws Exception {
        ThreadPoolImpl pool = new ThreadPoolImpl(2);
        pool.shutdown();
    }

}