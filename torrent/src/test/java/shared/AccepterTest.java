package shared;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

public class AccepterTest {

    private ServerSocket socket;

    @Before
    public void before() throws IOException {
        socket = new ServerSocket(9999);
    }

    @Test
    public void startAccept() throws IOException, InterruptedException {
        final AtomicInteger counter = new AtomicInteger();
        final Set<Long> threads = new ConcurrentSkipListSet<>();
        Accepter accepter = new Accepter(socket, session -> () -> {
            threads.add(Thread.currentThread().getId());
            counter.incrementAndGet();
        });

        accepter.start();

        Socket socket1 = new Socket("localhost", 9999);
        Socket socket2 = new Socket("localhost", 9999);
        Thread.sleep(1000);
        socket1.close();
        socket2.close();

        Assert.assertEquals(2, counter.get());
        Assert.assertEquals(2, threads.size());

        socket.close();
    }


    @Test()
    public void stopAccept() throws IOException, InterruptedException {
        Accepter accepter = new Accepter(socket, session -> () -> {
        });
        accepter.start();
        Socket socket1 = new Socket("localhost", 9999);
        Socket socket2 = new Socket("localhost", 9999);
        Thread.sleep(1000);
        accepter.stop();
        socket.close();

        Assert.assertTrue(socket.isClosed());
        Assert.assertEquals(-1, socket1.getInputStream().read());
        Assert.assertEquals(-1, socket2.getInputStream().read());
    }
}