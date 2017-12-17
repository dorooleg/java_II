package shared.protocol.messages.response;

import org.junit.Assert;
import org.junit.Test;
import shared.protocol.messages.FileInfo;
import shared.protocol.messages.request.ListRequestMessage;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class ListResponseMessageTest {
    @Test
    public void getFiles() throws Exception {
        final ListResponseMessage message = new ListResponseMessage(Collections.singletonList(new FileInfo(1, "java.jar", 100)));
        Assert.assertEquals(1, message.getFiles().size());
        Assert.assertEquals(1, message.getFiles().get(0).getId());
        Assert.assertEquals("java.jar", message.getFiles().get(0).getName());
        Assert.assertEquals(100, message.getFiles().get(0).getSize());
    }

}