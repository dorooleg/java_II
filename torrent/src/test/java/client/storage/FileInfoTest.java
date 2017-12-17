package client.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class FileInfoTest {

    @Test
    public void getCountBlocks() throws Exception {
        FileInfo info = new FileInfo("///", FileInfo.BLOCK_SIZE, new HashSet<>());
        Assert.assertEquals(1, info.getCountBlocks());
        info = new FileInfo("///", FileInfo.BLOCK_SIZE - 100, new HashSet<>());
        Assert.assertEquals(1, info.getCountBlocks());
        info = new FileInfo("///", FileInfo.BLOCK_SIZE + 100, new HashSet<>());
        Assert.assertEquals(2, info.getCountBlocks());
        info = new FileInfo("///", 3 * FileInfo.BLOCK_SIZE, new HashSet<>());
        Assert.assertEquals(3, info.getCountBlocks());
    }

    @Test
    public void isBlockAvailable() throws Exception {
        FileInfo info = new FileInfo("///", 4 * FileInfo.BLOCK_SIZE, new HashSet<>());
        Assert.assertEquals(false, info.isBlockAvailable(0));
        info = new FileInfo("///", 4 * FileInfo.BLOCK_SIZE, new HashSet<>(Collections.singletonList(1)));
        Assert.assertEquals(true, info.isBlockAvailable(1));
        Assert.assertEquals(false, info.isBlockAvailable(0));
    }

    @Test
    public void addAvailableBlock() throws Exception {
        final FileInfo info = new FileInfo("///", 4 * FileInfo.BLOCK_SIZE, new HashSet<>());
        Assert.assertEquals(false, info.isBlockAvailable(0));
        info.addAvailableBlock(0);
        Assert.assertEquals(true, info.isBlockAvailable(0));
    }

    @Test
    public void getAvailableBlocks() throws Exception {
        final FileInfo info = new FileInfo("///", 4 * FileInfo.BLOCK_SIZE, new HashSet<>(Arrays.asList(0, 1)));
        final Set<Integer> set = info.getAvailableBlocks();
        Assert.assertEquals(2, set.size());
        Assert.assertTrue(set.contains(0));
        Assert.assertTrue(set.contains(1));
    }

    @Test
    public void getBlockSize() throws Exception {
        final FileInfo info = new FileInfo("///", FileInfo.BLOCK_SIZE + 100, new HashSet<>());
        Assert.assertEquals(FileInfo.BLOCK_SIZE, info.getBlockSize(0));
        Assert.assertEquals(100, info.getBlockSize(1));
    }

    @Test
    public void getPath() throws Exception {
        final FileInfo info = new FileInfo("///", FileInfo.BLOCK_SIZE + 100, new HashSet<>());
        Assert.assertEquals("///", info.getPath());
    }

}