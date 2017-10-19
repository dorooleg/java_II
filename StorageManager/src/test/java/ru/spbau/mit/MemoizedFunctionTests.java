package ru.spbau.mit;

import org.junit.Test;
import ru.spbau.mit.util.CountingFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.*;

public class MemoizedFunctionTests extends AbstractMemoizedFunctionTest {
    @Test
    public void testTrivialGet() throws Throwable {
        doTest(
                CountingFunction.TRIVIAL_INVERSION,
                Arrays.asList(42, 0, -123),
                Arrays.asList(-42, 0, 123)
        );
    }

    @Test
    public void testNullReturn() throws Throwable {
        doTest(
                CountingFunction.INVERSION_NULL_AT_ZERO,
                Arrays.asList(42, 0, -123),
                Arrays.asList(-42, null, 123)
        );
    }

    @Test
    public void testThrow() throws Throwable {
        doTest(
                CountingFunction.INVERSION_THROW_AT_ZERO,
                Arrays.asList(42, 0, -123),
                Arrays.asList(-42, CountingFunction.EXCEPTION, 123)
        );
    }
}
