package ru.spbau.mit;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ShutdownTest {

    @Test(expected = LightExecutionException.class)
    public void shutdown() throws Exception {
        List<LightFuture<Integer>> list = new ArrayList<>();
        ThreadPoolImpl pool = new ThreadPoolImpl(5);
        for (int i = 0; i < 10; i++) {
            list.add(pool.add(() -> {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
                return 42;
            }));
        }

        pool.shutdown();
        for (LightFuture<Integer> future : list) {
            future.get();
        }
    }

}