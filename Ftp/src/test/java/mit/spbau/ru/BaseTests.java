package mit.spbau.ru;

import org.apache.poi.util.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class BaseTests {

    private static final int TEST_PORT = 5010;
    private static final String HOST = "localhost";

    @Test
    public void correctServerStart() {
        IServer server = new Server(TEST_PORT);
        Assert.assertTrue(server.start());
        server.stop();
    }

    @Test
    public void incorrectServerStart() {
        IServer server = new Server(TEST_PORT);
        Assert.assertTrue(server.start());
        IServer server2 = new Server(TEST_PORT);
        Assert.assertFalse(server2.start());
        server.stop();
        server2.stop();
    }

    @Test
    public void doubleServerStart() {
        IServer server = new Server(TEST_PORT);
        Assert.assertTrue(server.start());
        Assert.assertFalse(server.start());
        server.stop();
    }

    @Test
    public void correctClientStart() {
        IServer server = new Server(TEST_PORT);
        Assert.assertTrue(server.start());
        IClient client = new Client(HOST, TEST_PORT);
        Assert.assertTrue(client.connect());
        server.stop();
        client.disconnect();
    }

    @Test
    public void incorrectClientConnect() {
        IClient client = new Client(HOST, TEST_PORT);
        Assert.assertFalse(client.connect());
        client.disconnect();
    }

    @Test
    public void doubleClientConnect() {
        IServer server = new Server(TEST_PORT);
        Assert.assertTrue(server.start());
        IClient client = new Client(HOST, TEST_PORT);
        Assert.assertTrue(client.connect());
        Assert.assertFalse(client.connect());
        client.disconnect();
        server.stop();
    }

    @Test
    public void correctList() {
        IServer server = new Server(TEST_PORT);
        Assert.assertTrue(server.start());
        IClient client = new Client(HOST, TEST_PORT);
        Assert.assertTrue(client.connect());
        List<FileMetadata> files = client.executeList("./src/test/resources");
        Assert.assertNotNull(files);
        Assert.assertEquals(3, files.size());
        files = files.stream().sorted(Comparator.comparing(FileMetadata::getName)).collect(toList());
        Assert.assertEquals(".\\src\\test\\resources\\a", files.get(0).getName());
        Assert.assertEquals(".\\src\\test\\resources\\b", files.get(1).getName());
        Assert.assertEquals(".\\src\\test\\resources\\tmp", files.get(2).getName());
        Assert.assertEquals(false, files.get(0).isDirectory());
        Assert.assertEquals(false, files.get(1).isDirectory());
        Assert.assertEquals(true, files.get(2).isDirectory());
        server.stop();
        client.disconnect();
    }

    @Test
    public void incorrectList() {
        IServer server = new Server(TEST_PORT);
        Assert.assertTrue(server.start());
        IClient client = new Client(HOST, TEST_PORT);
        Assert.assertTrue(client.connect());
        List<FileMetadata> files = client.executeList("bara");
        Assert.assertNotNull(files);
        Assert.assertEquals(0, files.size());
        server.stop();
        client.disconnect();
    }

    @Test
    public void correctGet() throws IOException {
        IServer server = new Server(TEST_PORT);
        Assert.assertTrue(server.start());
        IClient client = new Client(HOST, TEST_PORT);
        Assert.assertTrue(client.connect());
        InputStream input = client.executeGet(".\\src\\test\\resources\\a");
        Assert.assertNotNull(input);
        byte[] data = IOUtils.toByteArray(input);
        Assert.assertEquals(1, data.length);
        Assert.assertEquals('a', data[0]);
        server.stop();
        client.disconnect();
    }

    @Test
    public void incorrectGet() throws IOException {
        IServer server = new Server(TEST_PORT);
        Assert.assertTrue(server.start());
        IClient client = new Client(HOST, TEST_PORT);
        Assert.assertTrue(client.connect());
        InputStream input = client.executeGet("abra");
        Assert.assertNotNull(input);
        byte[] data = IOUtils.toByteArray(input);
        Assert.assertEquals(0, data.length);
        server.stop();
        client.disconnect();
    }

    @Test
    public void getOnStopServer() throws IOException, InterruptedException {
        IServer server = new Server(TEST_PORT);
        Assert.assertTrue(server.start());
        IClient client = new Client(HOST, TEST_PORT);
        Assert.assertTrue(client.connect());
        server.stop();
        InputStream input = client.executeGet("abra");
        Assert.assertNull(input);
        client.disconnect();
    }

    @Test
    public void getOnDisconnectClient() throws IOException, InterruptedException {
        IServer server = new Server(TEST_PORT);
        Assert.assertTrue(server.start());
        IClient client = new Client(HOST, TEST_PORT);
        Assert.assertTrue(client.connect());
        client.disconnect();
        InputStream input = client.executeGet("abra");
        Assert.assertNull(input);
        server.stop();
    }

    @Test
    public void listOnStopServer() {
        IServer server = new Server(TEST_PORT);
        Assert.assertTrue(server.start());
        IClient client = new Client(HOST, TEST_PORT);
        Assert.assertTrue(client.connect());
        server.stop();
        List<FileMetadata> files = client.executeList("./src/test/resources");
        Assert.assertNull(files);
        client.disconnect();
    }

    @Test
    public void listOnDisconnectClient() {
        IServer server = new Server(TEST_PORT);
        Assert.assertTrue(server.start());
        IClient client = new Client(HOST, TEST_PORT);
        Assert.assertTrue(client.connect());
        client.disconnect();
        List<FileMetadata> files = client.executeList("./src/test/resources");
        Assert.assertNull(files);
        server.stop();
    }

}