package ru.spbau.mit;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

public class LightFutureTest {

    @Test(timeout=5000)
    public void sum() throws LightExecutionException {
        ThreadPoolImpl pool = new ThreadPoolImpl(Runtime.getRuntime().availableProcessors());
        List<LightFuture<Long>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            long finalI = i;
            LightFuture<Long> f = pool.add(() ->
                    LongStream.range(finalI * 1_000_000, (finalI + 1) * 1_000_000).sum()
            );
            list.add(f);
        }

        long sum = 0;
        for (LightFuture<Long> f : list) {
            sum += f.get();
        }

        pool.shutdown();
        Assert.assertEquals(linearSum(), sum);
    }

    private long linearSum() {
        return LongStream.range(0, 10 * 1_000_000).sequential().sum();
    }

    @Test
    public void linearSumTest() {
        linearSum();
    }

}