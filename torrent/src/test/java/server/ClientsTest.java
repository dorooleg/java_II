package server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import shared.protocol.messages.ClientInfo;
import shared.protocol.messages.FileInfo;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

public class ClientsTest {
    Clients clients;

    @Before
    public void before() {
        clients = new Clients();
    }

    @Test
    public void remove() throws Exception {
        ClientInfo info = new ClientInfo(new byte[4], (short)7770);
        clients.upload("tmp.txt", 500);
        clients.update(info, Collections.singletonList(1));
        Assert.assertEquals(1, clients.sources(1).size());
        Assert.assertEquals(info, clients.sources(1).get(0));
        clients.remove(info);
        Assert.assertEquals(0, clients.sources(1).size());
    }

    @Test
    public void list() throws Exception {
        clients.upload("tmp.txt", 500);
        clients.upload("a.txt", 300);
        clients.upload("b.txt", 400);
        List<FileInfo> list = clients.list();
        list.sort(Comparator.comparingInt(FileInfo::getId));

        Assert.assertEquals(1, list.get(0).getId());
        Assert.assertEquals(2, list.get(1).getId());
        Assert.assertEquals(3, list.get(2).getId());

        Assert.assertEquals("tmp.txt", list.get(0).getName());
        Assert.assertEquals("a.txt", list.get(1).getName());
        Assert.assertEquals("b.txt", list.get(2).getName());

        Assert.assertEquals(500, list.get(0).getSize());
        Assert.assertEquals(300, list.get(1).getSize());
        Assert.assertEquals(400, list.get(2).getSize());
    }

    @Test
    public void sources() throws Exception {
        ClientInfo info = new ClientInfo(new byte[4], (short)7770);
        ClientInfo info2 = new ClientInfo(new byte[4], (short)7760);
        clients.upload("tmp.txt", 500);
        clients.update(info, Collections.singletonList(1));
        clients.update(info2, Collections.singletonList(1));
        Assert.assertEquals(2, clients.sources(1).size());
        List<ClientInfo> sources = clients.sources(1);
        sources.sort(Comparator.comparingInt(ClientInfo::getPort));
        Assert.assertEquals(7760, sources.get(0).getPort());
        Assert.assertEquals(7770, sources.get(1).getPort());
    }
}