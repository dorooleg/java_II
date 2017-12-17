package client.protocol.messages.request;

import org.junit.Assert;
import org.junit.Test;
import shared.Accepter;

import static org.junit.Assert.*;

public class StatRequestMessageTest {
    @Test
    public void getFile() throws Exception {
        final StatRequestMessage message = new StatRequestMessage(1);
        Assert.assertEquals(1, message.getFile());
    }

}