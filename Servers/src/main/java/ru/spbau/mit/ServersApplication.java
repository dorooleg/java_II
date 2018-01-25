package ru.spbau.mit;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.servers.IServer;
import ru.spbau.mit.servers.ServerManager;
import ru.spbau.mit.servers.statistics.Statistic;
import ru.spbau.mit.servers.tcp.*;
import ru.spbau.mit.servers.udp.UdpFixedThreadPoolServer;
import ru.spbau.mit.servers.udp.UdpSingleThreadOnClientServer;

import java.io.IOException;
import java.util.List;

public class ServersApplication {
    private final static Logger logger = Logger.getLogger(ServersApplication.class);
    private final static int SERVER_MANAGER_PORT = 6660;
    private final static int SERVER_PORT = 6666;
    private final static int POOL_SIZE = 10;

    public static void main(final String[] args) throws IOException, InterruptedException {

        final ServerManager manager = new ServerManager(SERVER_MANAGER_PORT);
        manager.start();
        manager.accept();

        IServer server = null;

        while (true) {
            final ManagerProtos.RequestMessage message;

            try {
                message = manager.getMessage();
            } catch (InvalidProtocolBufferException e) {
                manager.accept();
                continue;
            }

            if (message == null) {
                logger.debug("Message is null");
                continue;
            }

            if (message.hasDisconnect()) {
                disconnect();
            }

            if (message.hasStart()) {
                server = start(server, message.getStart().getType());
            }

            if (message.hasStop()) {
                stop(server, manager);
                server = null;
            }
        }
    }

    private static void disconnect() {
        logger.debug("Disconnect");
        System.exit(0);
    }

    private static IServer start(IServer server, @NotNull final ManagerProtos.StartMessage.Type type) throws IOException, InterruptedException {
        if (server != null) {
            logger.error("Broken logic. Start without null server");
            server.stop();
        }

        server = createServer(type);

        server.start();

        return server;
    }

    public static void stop(final IServer server, @NotNull final ServerManager manager) throws IOException, InterruptedException {
        if (server == null) {
            logger.error("Broken logic. Stop with null server");
            return;
        }

        server.stop();
        final List<Statistic> statistics = server.getStatistics();

        final double averageProcessTime = statistics.stream().mapToDouble(Statistic::getProcessTime).average().orElse(0);
        final double averageRequestTime = statistics.stream().mapToDouble(Statistic::getRequestTime).average().orElse(0);
        manager.sendResponse(averageRequestTime, averageProcessTime);
    }


    @NotNull
    private static IServer createServer(@NotNull final ManagerProtos.StartMessage.Type type) throws IOException {
        switch (type) {
            case TCP_SINGLE_THREAD:
                return new SingleThreadServer(SERVER_PORT);
            case TCP_ASYNC:
                return new AsyncServer(SERVER_PORT);
            case TCP_SINGLE_THREAD_ON_CLIENT:
                return new SingleThreadOnClientServer(SERVER_PORT);
            case TCP_CACHED_THREAD:
                return new CachedThreadPoolServer(SERVER_PORT);
            case TCP_NON_BLOCKING:
                return new NonBlockingServer(SERVER_PORT, POOL_SIZE);
            case UDP_FIXED_POOL:
                return new UdpFixedThreadPoolServer(SERVER_PORT, POOL_SIZE);
            case UDP_SINGLE_THREAD_ON_REQUEST:
                return new UdpSingleThreadOnClientServer(SERVER_PORT);
        }

        return null;
    }
}
