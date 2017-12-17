package shared;

import org.junit.Assert;
import org.junit.Test;

import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerTest {
    @Test
    public void start() throws Exception {
        final AtomicInteger counter = new AtomicInteger();
        final Set<Long> threads = new ConcurrentSkipListSet<>();
        Server server = new Server(9999, session -> () -> {
            threads.add(Thread.currentThread().getId());
            counter.incrementAndGet();
        });
        server.start();

        Socket socket1 = new Socket("localhost", 9999);
        Socket socket2 = new Socket("localhost", 9999);
        Thread.sleep(1000);
        socket1.close();
        socket2.close();

        Assert.assertEquals(2, counter.get());
        Assert.assertEquals(2, threads.size());

        server.stop();
    }

    @Test
    public void stop() throws Exception {

        Server server = new Server(9999, session -> () -> {
        });
        server.start();

        Socket socket1 = new Socket("localhost", 9999);
        Socket socket2 = new Socket("localhost", 9999);

        Thread.sleep(1000);
        server.stop();

        Assert.assertEquals(-1, socket1.getInputStream().read());
        Assert.assertEquals(-1, socket2.getInputStream().read());
    }

}