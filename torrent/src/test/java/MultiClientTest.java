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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class MultiClientTest {

    private static final String EXPERIMENT_FILE = Paths.get("src", "test", "resources", "experiment.txt").toString();
    private static final String RESERVE_FILE = Paths.get("src", "test", "resources", "reserve.txt").toString();
    private final static int COUNT_CLIENTS = 5;
    private Server server;
    private List<TorrentClientWrapper> clients = new ArrayList<>();

    @Before
    public void before() throws IOException, ClassNotFoundException {
        final SessionHandler handler = new SessionHandler(new Clients());
        server = new Server(6660, handler);
        server.start();

        for (int i = 0; i < COUNT_CLIENTS; i++) {
            TorrentClientWrapper client = new TorrentClientWrapper(new Storage(), "localhost", (short) 6660, (short) (7770 + i));
            client.start();
            clients.add(client);
        }
    }

    @After
    public void after() throws IOException {
        server.stop();
        for (TorrentClientWrapper client : clients) {
            client.stop();
        }
    }

    @Test
    public void multipleReader() throws IOException, InterruptedException {
        for (int i = 0; i < COUNT_CLIENTS; i++) {
            try {
                FileUtils.forceDelete(new File(RESERVE_FILE + i));
            } catch (FileNotFoundException ignored) {
            }
        }

        clients.get(0).upload(EXPERIMENT_FILE, "file");
        clients.get(0).update();


        List<Thread> threads = new ArrayList<>();
        Set<DownloadStatus> statuses = new ConcurrentSkipListSet<>();
        for (int i = 0; i < COUNT_CLIENTS; i++) {
            int finalI = i;
            Thread.sleep(100);
            Thread thread = new Thread(() -> {
                try {
                    statuses.add(clients.get(finalI).download(RESERVE_FILE + finalI, 1));
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            threads.add(thread);

            for (int j = 0; j <= i; j++) {
                clients.get(j).update();
            }
        }

        for (Thread thread : threads) {
            thread.join();
        }

        for (DownloadStatus status : statuses) {
            Assert.assertEquals(DownloadStatus.FULL, status);
        }

        File file1 = new File(EXPERIMENT_FILE);
        for (int i = 1; i < COUNT_CLIENTS; i++) {
            File file2 = new File(RESERVE_FILE + i);
            Assert.assertTrue(FileUtils.contentEquals(file1, file2));
        }
    }
}
