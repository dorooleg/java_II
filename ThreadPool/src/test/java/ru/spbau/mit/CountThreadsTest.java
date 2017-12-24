package ru.spbau.mit;

import org.junit.Before;
import org.junit.Test;

public class CountThreadsTest {

    private volatile int counter;
    private volatile boolean done;

    @Before
    public void setUp() {
        counter = 0;
        done = false;
    }

    private void countThreadsTest(int countThreads) {
        ThreadPoolImpl pool = new ThreadPoolImpl(countThreads);
        for (int i = 0; i < countThreads; i++) {
            pool.add(() -> {
                counter++;
                synchronized (this) {
                    while (!done) {
                        try {
                            wait();
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
                return null;
            });
        }
        while (counter != countThreads) ;
        done = true;
        synchronized (this) {
            notifyAll();
        }
        pool.shutdown();
    }

    private void countThreadsTestByPool(int countThreads) {
        ThreadPoolImpl pool = new ThreadPoolImpl(countThreads);
        for (int i = 0; i < countThreads; i++) {
            pool.add(() -> {
                counter++;
                synchronized (pool) {
                    while (!done) {
                        try {
                            pool.wait();
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
                return null;
            });
        }
        while (counter != countThreads) ;
        done = true;
        synchronized (pool) {
            pool.notifyAll();
        }
        pool.shutdown();
    }

    @Test(timeout = 10000)
    public void oneThreads() {
        countThreadsTest(1);
    }

    @Test(timeout = 10000)
    public void twoThreads() {
        countThreadsTest(2);
    }

    @Test(timeout = 10000)
    public void tenThreads() {
        countThreadsTest(10);
    }

    @Test(timeout = 10000)
    public void hundredThreads() {
        countThreadsTest(100);
    }

    @Test(timeout = 10000)
    public void oneThreadsByPool() {
        countThreadsTestByPool(1);
    }

    @Test(timeout = 10000)
    public void twoThreadsByPool() {
        countThreadsTestByPool(2);
    }

    @Test(timeout = 10000)
    public void tenThreadsByPool() {
        countThreadsTestByPool(10);
    }

    @Test(timeout = 10000)
    public void hundredThreadsByPool() {
        countThreadsTestByPool(100);
    }

}