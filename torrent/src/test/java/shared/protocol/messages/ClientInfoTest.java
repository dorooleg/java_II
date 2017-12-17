package shared.protocol.messages;

import org.junit.Assert;
import org.junit.Test;
import shared.protocol.messages.response.SourcesResponseMessage;

import java.util.Arrays;

public class ClientInfoTest {
    @Test
    public void getIp() throws Exception {
        final byte[] ip = new byte[4];
        ip[0] = 0x55;
        ip[3] = 0x77;

        final ClientInfo message = new ClientInfo(ip, (short)10);
        Assert.assertEquals(0x55, message.getIp()[0]);
        Assert.assertEquals(0x77, message.getIp()[3]);
    }

    @Test
    public void getPort() throws Exception {
        final ClientInfo info = new ClientInfo(new byte[4], (short) 777);
        Assert.assertEquals(777, info.getPort());
    }

    @Test
    public void compareTo() throws Exception {
        final byte[] ip1 = new byte[4];
        ip1[0] = 0x55;
        ip1[3] = 0x77;

        final byte[] ip2 = new byte[4];
        ip2[0] = 0x55;
        ip2[3] = 0x77;

        final byte[] ip3 = new byte[4];
        ip3[0] = 0x55;
        ip3[3] = 0x78;

        Assert.assertEquals(0, new ClientInfo(ip1, (short)777).compareTo(new ClientInfo(ip2, (short)777)));
        Assert.assertEquals(1, new ClientInfo(ip1, (short)778).compareTo(new ClientInfo(ip2, (short)777)));
        Assert.assertEquals(-1, new ClientInfo(ip1, (short)776).compareTo(new ClientInfo(ip2, (short)777)));
        Assert.assertEquals(0, new ClientInfo(ip1, (short)777).compareTo(new ClientInfo(ip2, (short)777)));
        Assert.assertEquals(1, new ClientInfo(ip1, (short)778).compareTo(new ClientInfo(ip2, (short)777)));
        Assert.assertEquals(1, new ClientInfo(ip3, (short)777).compareTo(new ClientInfo(ip2, (short)777)));
        Assert.assertEquals(-1, new ClientInfo(ip2, (short)777).compareTo(new ClientInfo(ip3, (short)777)));
    }

}