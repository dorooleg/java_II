package client.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StorageTest {

    private static final String EXPERIMENT_FILE = Paths.get("src", "test", "resources", "experiment.txt").toString();

    IStorage storage;

    @Before
    public void storage() {
        storage = new Storage();
    }

    @Test
    public void addFile() throws Exception {
        storage.addFile(1, EXPERIMENT_FILE, Files.size(Paths.get(EXPERIMENT_FILE)));
        Assert.assertTrue(storage.isExists(1));
        Assert.assertEquals(EXPERIMENT_FILE, storage.getPath(1));
        Assert.assertEquals(1, storage.getFiles().size());
        Assert.assertEquals(1, storage.getFiles().get(0).intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFileException() throws IOException {
        storage.addFile(1, EXPERIMENT_FILE, Files.size(Paths.get(EXPERIMENT_FILE)));
        storage.addFile(1, EXPERIMENT_FILE, Files.size(Paths.get(EXPERIMENT_FILE)));
    }

    @Test
    public void reserveFile() throws Exception {
        final String reserve = Paths.get("src", "test", "resources", "reserve.txt").toString();
        storage.reserveFile(1, reserve, 1000);
        Assert.assertEquals(reserve, storage.getPath(1));
        Assert.assertEquals(1, storage.getCountBlocks(1));
        Assert.assertEquals(0, storage.getAvailableBlocks(1).size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void reserveFileException() throws IOException {
        final String reserve = Paths.get("src", "test", "resources", "reserve.txt").toString();
        storage.reserveFile(1, reserve, 1000);
        storage.reserveFile(1, reserve, 1000);
    }

    @Test
    public void readBlock() throws Exception {
        storage.addFile(1, EXPERIMENT_FILE, Files.size(Paths.get(EXPERIMENT_FILE)));
        byte[] buffer = storage.readBlock(1, 0);
        Assert.assertEquals('j', buffer[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void readBlockFileException() throws Exception {
        storage.addFile(1, EXPERIMENT_FILE, Files.size(Paths.get(EXPERIMENT_FILE)));
        byte[] buffer = storage.readBlock(2, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void readBlockException() throws Exception {
        final String reserve = Paths.get("src", "test", "resources", "reserve.txt").toString();
        storage.reserveFile(2, reserve, 1000);
        byte[] buffer = storage.readBlock(2, 0);
    }

    @Test
    public void writeBlock() throws Exception {
        storage.addFile(1, EXPERIMENT_FILE, Files.size(Paths.get(EXPERIMENT_FILE)));
        byte[] buffer = storage.readBlock(1, 0);
        final String reserve = Paths.get("src", "test", "resources", "reserve.txt").toString();
        storage.reserveFile(2, reserve, 1000);
        storage.writeBlock(2, 0, buffer);
        buffer = storage.readBlock(2, 0);
        Assert.assertEquals('j', buffer[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeBlockException() throws Exception {
        storage.addFile(1, EXPERIMENT_FILE, Files.size(Paths.get(EXPERIMENT_FILE)));
        byte[] buffer = storage.readBlock(1, 0);
        final String reserve = Paths.get("src", "test", "resources", "reserve.txt").toString();
        storage.reserveFile(2, reserve, 1000);
        storage.writeBlock(2, 10, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCountBlocksException() throws Exception {
        storage.getCountBlocks(300);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAvailableBlocksException() throws Exception {
        storage.getAvailableBlocks(200);
    }
}