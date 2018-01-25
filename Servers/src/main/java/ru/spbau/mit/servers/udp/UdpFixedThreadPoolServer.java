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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.spbau.mit.Utility.MAX_UDP_SIZE;


public class UdpFixedThreadPoolServer implements IServer {
    @org.jetbrains.annotations.NotNull
    private final static Logger logger = Logger.getLogger(UdpFixedThreadPoolServer.class);

    @NotNull
    private final ExecutorService pool;
    @NotNull
    private final List<Statistic> statistics = Collections.synchronizedList(new ArrayList<>());
    private final int port;
    private DatagramSocket serverSocket;

    public UdpFixedThreadPoolServer(final int port, final int poolSize) throws IOException {
        logger.debug("create");
        this.port = port;
        pool = Executors.newFixedThreadPool(poolSize);
    }

    public void start() throws IOException {
        logger.debug("start");
        serverSocket = new DatagramSocket(port);
        statistics.clear();
        pool.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                DatagramPacket packet = new DatagramPacket(new byte[MAX_UDP_SIZE], MAX_UDP_SIZE);

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

                new UdpTask(packet, serverSocket, statistics).run();
            }
        });
    }

    public void stop() throws IOException, InterruptedException {
        logger.debug("stop");
        pool.shutdownNow();
        serverSocket.close();
    }

    @NotNull
    public List<Statistic> getStatistics() {
        return statistics;
    }
}
