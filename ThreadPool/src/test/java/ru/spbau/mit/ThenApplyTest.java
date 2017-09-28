package ru.spbau.mit;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;


public class ThenApplyTest {

    @Test
    public void thenApply() throws Exception {
        ThreadPoolImpl pool = new ThreadPoolImpl(2);

        LightFuture<Integer> sum = pool.add(() -> 0);

        for (int i = 0; i < 100; i++) {
            sum = sum.thenApply(a -> {
                try {
                    Thread.sleep(Math.abs(new Random(0).nextInt() % 10));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return a + 1;
            });
        }

        Assert.assertEquals(new Integer(100), sum.get());
        pool.shutdown();
    }
}
