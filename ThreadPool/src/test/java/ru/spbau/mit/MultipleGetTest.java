package ru.spbau.mit;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MultipleGetTest {

    @Test(timeout = 1000)
    public void get() throws Exception {
        List<LightFuture<Integer>> list = new ArrayList<>();
        ThreadPoolImpl pool = new ThreadPoolImpl(5);
        for (int i = 0; i < 100; i++) {
            list.add(pool.add(() -> 5));
        }
        int sum = 0;
        for (int i = 0; i < 3; i++) {
            for (LightFuture<Integer> future : list) {
                sum += future.get();
            }
        }
        pool.shutdown();
        Assert.assertEquals(1500, sum);
    }

}