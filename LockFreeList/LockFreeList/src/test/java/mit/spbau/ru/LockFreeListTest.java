package mit.spbau.ru;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class LockFreeListTest {

    private ILockFreeList<Integer> set;

    @Before
    public void setUp() {
        set = new LockFreeList<>();
    }

    @Test
    public void append() throws Exception {
        set.append(10);
        Assert.assertTrue(set.contains(10));
        set.append(20);
        Assert.assertTrue(set.contains(20));
        set.append(5);
        Assert.assertTrue(set.contains(5));
        set.append(5);
        set.append(10);
        set.append(20);
        Assert.assertTrue(set.contains(5));
        Assert.assertTrue(set.contains(10));
        Assert.assertTrue(set.contains(20));
    }

    @Test
    public void remove() throws Exception {
        append();
        set.append(40);
        Assert.assertTrue(set.contains(40));
        set.append(1);
        Assert.assertTrue(set.contains(1));
        Assert.assertTrue(set.remove(10));
        Assert.assertFalse(set.contains(10));
        Assert.assertTrue(set.remove(1));
        Assert.assertFalse(set.contains(1));
        Assert.assertTrue(set.remove(40));
        Assert.assertFalse(set.contains(40));
        Assert.assertTrue(set.remove(5));
        Assert.assertFalse(set.contains(5));
        Assert.assertTrue(set.remove(20));
        Assert.assertFalse(set.contains(20));
        Assert.assertFalse(set.remove(20));
    }

    @Test
    public void contains() throws Exception {
        Assert.assertFalse(set.contains(0));
    }

    @Test
    public void isEmpty() throws Exception {
        Assert.assertTrue(set.isEmpty());
        append();
        Assert.assertFalse(set.isEmpty());
        Assert.assertTrue(set.remove(5));
        Assert.assertTrue(set.remove(10));
        Assert.assertTrue(set.remove(20));
        Assert.assertTrue(set.isEmpty());
        remove();
        Assert.assertTrue(set.isEmpty());
    }


}