package ru.spbau.mit.algorithms;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class SortsTest {
    @Test
    public void bubbleSort() throws Exception {
        Assert.assertEquals(Arrays.asList(0, 1), Sorts.bubbleSort(Arrays.asList(1, 0)));
        Assert.assertEquals(Collections.singletonList(0), Sorts.bubbleSort(Collections.singletonList(0)));
        Assert.assertEquals(Collections.emptyList(), Sorts.bubbleSort(Collections.emptyList()));
        Assert.assertEquals(Arrays.asList(0, 1, 2), Sorts.bubbleSort(Arrays.asList(1, 2, 0)));
    }

}