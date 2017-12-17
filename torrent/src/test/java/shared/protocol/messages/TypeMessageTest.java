package shared.protocol.messages;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeMessageTest {

    @Test(expected = IllegalArgumentException.class)
    public void getEnumException() {
        TypeMessage.getEnum(0);
    }

    @Test
    public void getEnum() throws Exception {
        Assert.assertEquals(TypeMessage.LIST, TypeMessage.getEnum(1));
        Assert.assertEquals(TypeMessage.UPLOAD, TypeMessage.getEnum(2));
        Assert.assertEquals(TypeMessage.SOURCES, TypeMessage.getEnum(3));
        Assert.assertEquals(TypeMessage.UPDATE, TypeMessage.getEnum(4));
    }

    @Test
    public void getId() throws Exception {
        Assert.assertEquals(1, TypeMessage.LIST.getId());
        Assert.assertEquals(2, TypeMessage.UPLOAD.getId());
        Assert.assertEquals(3, TypeMessage.SOURCES.getId());
        Assert.assertEquals(4, TypeMessage.UPDATE.getId());
    }

}