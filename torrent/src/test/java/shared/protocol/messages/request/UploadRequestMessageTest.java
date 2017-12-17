package shared.protocol.messages.request;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class UploadRequestMessageTest {
    @Test
    public void getName() throws Exception {
        final UploadRequestMessage message = new UploadRequestMessage("instruction.txt", 1);
        Assert.assertEquals("instruction.txt", message.getName());
    }

    @Test
    public void getSize() throws Exception {
        final UploadRequestMessage message = new UploadRequestMessage("", 255);
        Assert.assertEquals(255, message.getSize());
    }

}