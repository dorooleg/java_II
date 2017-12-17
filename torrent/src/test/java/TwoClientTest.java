import client.TorrentClientWrapper;
import client.storage.Storage;
import client.tasks.DownloadStatus;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import server.Clients;
import server.handlers.SessionHandler;
import shared.Server;
import shared.protocol.messages.ClientInfo;
import shared.protocol.messages.FileInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

public class TwoClientTest {

    private static final String EXPERIMENT_FILE = Paths.get("src", "test", "resources", "experiment.txt").toString();
    private static final String EXPERIMENT_FILE2 = Paths.get("src", "test", "resources", "experiment2.txt").toString();
    private static final String RESERVE_FILE = Paths.get("src", "test", "resources", "reserve.txt").toString();
    private Server server;
    private TorrentClientWrapper client1;
    private TorrentClientWrapper client2;

    @Before
    public void before() throws IOException, ClassNotFoundException {
        final SessionHandler handler = new SessionHandler(new Clients());
        server = new Server(6660, handler);
        server.start();

        client1 = new TorrentClientWrapper(new Storage(), "localhost", (short) 6660, (short) 7770);
        client1.start();

        client2 = new TorrentClientWrapper(new Storage(), "localhost", (short) 6660, (short) 8880);
        client2.start();
    }

    @After
    public void after() throws IOException {
        server.stop();
        client2.stop();
    }

    @Test
    public void testDownload() throws IOException, InterruptedException {
        try {
            FileUtils.forceDelete(new File(RESERVE_FILE));
        } catch (FileNotFoundException ignored) {
        }

        client1.upload(EXPERIMENT_FILE, "file");
        client1.update();
        List<FileInfo> files = client2.list();
        Assert.assertEquals(1, files.size());
        DownloadStatus download = client2.download(RESERVE_FILE, files.get(0).getId());
        Assert.assertEquals(DownloadStatus.FULL, download);
        client2.update();
        List<ClientInfo> sources = client1.sources(files.get(0).getId());
        Assert.assertEquals(2, sources.size());
        sources.sort(Comparator.comparingInt(ClientInfo::getPort));
        Assert.assertEquals(7770, sources.get(0).getPort());
        Assert.assertEquals(8880, sources.get(1).getPort());

        File file1 = new File(EXPERIMENT_FILE);
        File file2 = new File(RESERVE_FILE);
        Assert.assertTrue(FileUtils.contentEquals(file1, file2));

        client2.upload(EXPERIMENT_FILE2, "exp");
        Assert.assertEquals(2, client2.list().size());
    }
}
