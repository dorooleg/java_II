package shared.protocol.messages;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileInfoTest {

    private static FileInfo info;

    @BeforeClass
    public static void beforeClass() {
        info = new FileInfo(10, "tmp.txt", 1500);
    }

    @Test
    public void getId() throws Exception {
        Assert.assertEquals(10, info.getId());
    }

    @Test
    public void getName() throws Exception {
        Assert.assertEquals("tmp.txt", info.getName());
    }

    @Test
    public void getSize() throws Exception {
        Assert.assertEquals(1500, info.getSize());
    }

}