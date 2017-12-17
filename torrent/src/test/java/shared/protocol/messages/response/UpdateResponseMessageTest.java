package shared.protocol.messages.response;

import org.junit.Assert;
import org.junit.Test;
import shared.protocol.messages.request.UploadRequestMessage;

import static org.junit.Assert.*;

public class UpdateResponseMessageTest {
    @Test
    public void getStatus() throws Exception {
        final UpdateResponseMessage message = new UpdateResponseMessage(true);
        Assert.assertEquals(true, message.getStatus());
    }

}