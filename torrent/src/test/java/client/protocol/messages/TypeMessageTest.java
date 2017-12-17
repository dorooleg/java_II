package client.protocol.messages;

import org.junit.Assert;
import org.junit.Test;

public class TypeMessageTest {
    @Test
    public void getEnum() throws Exception {
        Assert.assertEquals(TypeMessage.GET, TypeMessage.getEnum(2));
        Assert.assertEquals(TypeMessage.STAT, TypeMessage.getEnum(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getEnumException() throws Exception {
        TypeMessage.getEnum(0);
    }

    @Test
    public void getId() throws Exception {
        Assert.assertEquals(2, TypeMessage.GET.getId());
        Assert.assertEquals(1, TypeMessage.STAT.getId());
    }

}