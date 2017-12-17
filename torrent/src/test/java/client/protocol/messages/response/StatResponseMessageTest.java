package client.protocol.messages.response;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

public class StatResponseMessageTest {
    @Test
    public void getBlocks() throws Exception {
        final StatResponseMessage message = new StatResponseMessage(new HashSet<>(Arrays.asList(1, 2)));
        Assert.assertEquals(2, message.getBlocks().size());
        Assert.assertTrue(message.getBlocks().contains(1));
        Assert.assertTrue(message.getBlocks().contains(2));
    }

}