package shared.protocol.messages.request;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class UpdateRequestMessageTest {
    @Test
    public void getPort() throws Exception {
        final UpdateRequestMessage message = new UpdateRequestMessage((short)7770, new ArrayList<>());
        Assert.assertEquals(7770, message.getPort());
    }

    @Test
    public void getFiles() throws Exception {
        final UpdateRequestMessage message = new UpdateRequestMessage((short)7770, Arrays.asList(1, 3, 2));
        List<Integer> files = message.getFiles();
        Assert.assertEquals(3, files.size());
        Assert.assertEquals(1, files.get(0).intValue());
        Assert.assertEquals(3, files.get(1).intValue());
        Assert.assertEquals(2, files.get(2).intValue());
    }

}