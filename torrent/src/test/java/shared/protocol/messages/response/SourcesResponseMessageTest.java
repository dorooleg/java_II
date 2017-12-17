package shared.protocol.messages.response;

import org.junit.Assert;
import org.junit.Test;
import shared.protocol.messages.ClientInfo;

import java.util.Arrays;

import static org.junit.Assert.*;

public class SourcesResponseMessageTest {
    @Test
    public void getClients() throws Exception {
        final byte[] ip = new byte[4];
        ip[0] = 0x55;
        ip[3] = 0x77;

        final SourcesResponseMessage message = new SourcesResponseMessage(Arrays.asList(new ClientInfo(ip, (short)10)));
        Assert.assertEquals(1, message.getClients().size());
        Assert.assertEquals(0x55, message.getClients().get(0).getIp()[0]);
        Assert.assertEquals(0x77, message.getClients().get(0).getIp()[3]);
        Assert.assertEquals(10, message.getClients().get(0).getPort());
    }

}