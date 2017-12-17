import client.TorrentClientWrapper;
import client.Utility;
import client.storage.Storage;
import client.tasks.DownloadStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import server.Clients;
import server.handlers.SessionHandler;
import shared.Server;
import shared.protocol.messages.ClientInfo;
import shared.protocol.messages.FileInfo;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

public class OneClientTest {

    private static final String EXPERIMENT_FILE = Paths.get("src", "test", "resources", "experiment.txt").toString();
    private static final String EXPERIMENT_FILE2 = Paths.get("src", "test", "resources", "experiment2.txt").toString();
    private static final String RESERVE_FILE = Paths.get("src", "test", "resources", "reserve.txt").toString();
    private Server server;
    private TorrentClientWrapper client;

    @Before
    public void before() throws IOException, ClassNotFoundException {
        final SessionHandler handler = new SessionHandler(new Clients());
        server = new Server(6660, handler);
        server.start();

        client = new TorrentClientWrapper(new Storage(), "localhost", (short) 6660, (short) 7770);
        client.start();
    }

    @After
    public void after() throws IOException {
        server.stop();
        client.stop();
    }

    @Test
    public void simpleEmptyCrashTest() throws IOException, InterruptedException {
        List<FileInfo> list = client.list();
        Assert.assertEquals(0, list.size());
        client.update();
        List<ClientInfo> sources = client.sources(100);
        Assert.assertEquals(0, sources.size());
        DownloadStatus download = client.download(RESERVE_FILE, 1);
        Assert.assertEquals(DownloadStatus.FILE_NOT_FOUND, download);
    }

    @Test
    public void logicTest() throws IOException, ClassNotFoundException {
        client.upload(EXPERIMENT_FILE, "file");
        List<FileInfo> list = client.list();
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(1, list.get(0).getId());
        Assert.assertEquals("file", list.get(0).getName());
        Assert.assertEquals(65003520, list.get(0).getSize());
        client.update();
        List<ClientInfo> sources = client.sources(1);
        Assert.assertEquals(1, sources.size());
        Assert.assertEquals("127.0.0.1", Utility.ipToString(sources.get(0).getIp()));
        Assert.assertEquals(7770, sources.get(0).getPort());
        client.stop();

        client = new TorrentClientWrapper(new Storage(), "localhost", (short) 6660, (short) 7770);
        client.start();
        client.update();

        sources = client.sources(1);
        Assert.assertEquals(0, sources.size());
    }

    @Test
    public void twoFiles() throws IOException {
        client.upload(EXPERIMENT_FILE, "file");
        client.upload(EXPERIMENT_FILE2, "file");

        List<FileInfo> list = client.list();
        list.sort(Comparator.comparingInt(FileInfo::getId));
        Assert.assertEquals(2, list.size());
        Assert.assertEquals(1, list.get(0).getId());
        Assert.assertEquals("file", list.get(0).getName());
        Assert.assertEquals(65003520, list.get(0).getSize());
        Assert.assertEquals(2, list.get(1).getId());
        Assert.assertEquals("file", list.get(1).getName());
        Assert.assertEquals(65003520, list.get(1).getSize());
    }
}
