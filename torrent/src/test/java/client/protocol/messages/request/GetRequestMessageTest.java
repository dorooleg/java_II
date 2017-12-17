package client.protocol.messages.request;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class GetRequestMessageTest {
    @Test
    public void test() throws Exception {
        final GetRequestMessage requestMessage = new GetRequestMessage(1, 2);
        Assert.assertEquals(1, requestMessage.getFile());
        Assert.assertEquals(2, requestMessage.getBlock());
    }

}