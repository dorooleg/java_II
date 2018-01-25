package ru.spbau.mit;

import com.google.protobuf.InvalidProtocolBufferException;
import ru.spbau.mit.servers.IServer;
import ru.spbau.mit.servers.ServerManager;
import ru.spbau.mit.servers.statistics.Statistic;
import ru.spbau.mit.servers.tcp.*;
import ru.spbau.mit.servers.udp.UdpFixedThreadPoolServer;
import ru.spbau.mit.servers.udp.UdpSingleThreadOnClientServer;

import java.io.IOException;
import java.util.List;

public class ServersApplication {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerManager manager = new ServerManager(6660);

        manager.start();


        System.out.println("Start server manager with port: 6660");

        manager.accept();

        IServer server = null;

        while (true) {
            final ManagerProtos.RequestMessage message;
            try {
                message = manager.getMessage();
            } catch (InvalidProtocolBufferException ignored) {
                System.out.println("Invalid protocol");
                continue;
            }

            if (message == null) {
                continue;
            }

            if (message.hasDisconnect()) {
                System.out.println("Disconnect");
                System.exit(0);
            }

            if (message.hasStart()) {

                if (server != null) {
                    server.stop();
                }

                switch (message.getStart().getType()) {
                    case TCP_SINGLE_THREAD:
                        server = new SingleThreadServer(6666);
                        System.out.println("Created SingleThreadServer");
                        break;
                    case TCP_ASYNC:
                        server = new AsyncServer(6666);
                        System.out.println("Created AsyncServer");
                        break;
                    case TCP_SINGLE_THREAD_ON_CLIENT:
                        server = new SingleThreadOnClientServer(6666);
                        System.out.println("Created SingleThreadOnClientServer");
                        break;
                    case TCP_CACHED_THREAD:
                        server = new CachedThreadPoolServer(6666);
                        System.out.println("Created CachedThreadPoolServer");
                        break;
                    case TCP_NON_BLOCKING:
                        server = new NonBlockingServer(6666, 10);
                        System.out.println("Created NonBlockingServer");
                        break;
                    case UDP_FIXED_POOL:
                        server = new UdpFixedThreadPoolServer(6666, 10);
                        System.out.println("Created UdpFixedThreadPoolServer");
                        break;
                    case UDP_SINGLE_THREAD_ON_REQUEST:
                        server = new UdpSingleThreadOnClientServer(6666);
                        System.out.println("Created UdpSingleThreadOnClientServer");
                        break;
                }

                server.start();
            }

            if (message.hasStop()) {
                if (server == null) {
                    System.err.println("Broken logic!");
                    continue;
                }
                server.stop();
                List<Statistic> statistics = server.getStatistics();

                final double averageProcessTime = statistics.stream().mapToDouble(Statistic::getProcessTime).average().orElse(0);
                final double averageRequestTime = statistics.stream().mapToDouble(Statistic::getRequestTime).average().orElse(0);
                manager.sendResponse(averageRequestTime, averageProcessTime);
                server = null;
            }
        }



    }
}
