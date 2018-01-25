package ru.spbau.mit.servers.tcp;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.ArrayProtos;
import ru.spbau.mit.algorithms.Sorts;
import ru.spbau.mit.protocol.TcpProtocol;
import ru.spbau.mit.servers.IServer;
import ru.spbau.mit.servers.statistics.Statistic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SingleThreadServer implements IServer {
    @NotNull
    private final static Logger logger = Logger.getLogger(SingleThreadServer.class);

    @NotNull
    private final List<Statistic> statistics = new ArrayList<>();
    private final int port;
    private ServerSocket serverSocket;
    private Thread thread;

    public SingleThreadServer(final int port) throws IOException {
        logger.debug("create");
        this.port = port;
    }

    public void start() throws IOException {
        logger.debug("start");
        serverSocket = new ServerSocket(port);
        statistics.clear();
        thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try (final Socket socket = serverSocket.accept()) {

                    long requestTime = System.nanoTime();
                    final DataInputStream input = new DataInputStream(socket.getInputStream());
                    final DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                    final TcpProtocol protocol = new TcpProtocol(input, output);
                    final ArrayProtos.ArrayMessage array = protocol.getArray();

                    if (array == null) {
                        logger.debug("array is null");
                        break;
                    }

                    long processTime = System.nanoTime();
                    final List<Integer> sortedArray = Sorts.bubbleSort(array.getValuesList());
                    processTime = System.nanoTime() - processTime;

                    protocol.sendArray(ArrayProtos.ArrayMessage.newBuilder().addAllValues(sortedArray).build());

                    requestTime = System.nanoTime() - requestTime;
                    statistics.add(new Statistic(requestTime, processTime));
                } catch (IOException ignored) {
                    logger.debug("Client socket connect error");
                }
            }
        });

        thread.start();
    }

    public void stop() throws InterruptedException, IOException {
        logger.debug("stop");
        thread.interrupt();
        serverSocket.close();
    }

    @NotNull
    public List<Statistic> getStatistics() {
        return statistics;
    }
}
