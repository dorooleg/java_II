package client.protocol.messages.response;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class GetResponseMessageTest {
    @Test
    public void getBytes() throws Exception {
        final GetResponseMessage message = new GetResponseMessage(new byte[] { 1, 2 });
        Assert.assertEquals(2, message.getBytes()[1]);
    }

}