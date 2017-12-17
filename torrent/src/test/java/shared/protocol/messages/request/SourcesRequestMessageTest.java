package shared.protocol.messages.request;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SourcesRequestMessageTest {
    @Test
    public void getId() throws Exception {
        final SourcesRequestMessage message = new SourcesRequestMessage(10);
        Assert.assertEquals(10, message.getId());
    }

}