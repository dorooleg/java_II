package shared.protocol.messages.response;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class UploadResponseMessageTest {
    @Test
    public void getId() throws Exception {
        final UploadResponseMessage message = new UploadResponseMessage(100);
        Assert.assertEquals(100, message.getId());
    }

}