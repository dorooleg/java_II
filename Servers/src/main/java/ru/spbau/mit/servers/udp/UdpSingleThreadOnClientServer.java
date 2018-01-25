package ru.spbau.mit.servers.udp;

import com.sun.istack.internal.NotNull;
import org.apache.log4j.Logger;
import ru.spbau.mit.servers.IServer;
import ru.spbau.mit.servers.statistics.Statistic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.spbau.mit.Utility.MAX_UDP_SIZE;

public class UdpSingleThreadOnClientServer implements IServer {
    @org.jetbrains.annotations.NotNull
    private final static Logger logger = Logger.getLogger(UdpSingleThreadOnClientServer.class);
    @NotNull
    private final List<Statistic> statistics = Collections.synchronizedList(new ArrayList<>());
    @NotNull
    private final List<Thread> pool = new ArrayList<>();
    private final int port;
    private DatagramSocket serverSocket;
    private Thread thread;

    public UdpSingleThreadOnClientServer(final int port) throws IOException {
        logger.debug("create");
        this.port = port;
    }

    public void start() throws IOException {
        serverSocket = new DatagramSocket(port);
        statistics.clear();

        thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {

                final DatagramPacket packet = new DatagramPacket(new byte[MAX_UDP_SIZE], MAX_UDP_SIZE);

                try {
                    serverSocket.receive(packet);
                } catch (IOException ignored) {
                    logger.debug("receive IO error");
                    continue;
                }

                if (packet.getPort() < 0) {
                    logger.debug("negative port");
                    continue;
                }

                pool.add(new Thread(new UdpTask(packet, serverSocket, statistics)));
                pool.get(pool.size() - 1).start();
            }
        });

        thread.start();
    }

    public void stop() throws IOException, InterruptedException {
        for (Thread thread : pool) {
            thread.interrupt();
        }

        thread.interrupt();
        serverSocket.close();
        pool.clear();
        thread.join();
    }

    @NotNull
    public List<Statistic> getStatistics() {
        return statistics;
    }
}
