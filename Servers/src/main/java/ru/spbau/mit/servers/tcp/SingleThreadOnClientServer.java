package ru.spbau.mit.servers.tcp;

import com.sun.istack.internal.NotNull;
import org.apache.log4j.Logger;
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
import java.util.Collections;
import java.util.List;

public class SingleThreadOnClientServer implements IServer {
    @org.jetbrains.annotations.NotNull
    private final static Logger logger = Logger.getLogger(SingleThreadOnClientServer.class);

    @NotNull
    private final List<Statistic> statistics = Collections.synchronizedList(new ArrayList<>());
    private final int port;
    private ServerSocket serverSocket;
    private Thread thread;
    @NotNull
    private List<Thread> pool = new ArrayList<>();

    public SingleThreadOnClientServer(final int port) throws IOException {
        logger.debug("create");
        this.port = port;
    }

    public void start() throws IOException {
        logger.debug("start");
        serverSocket = new ServerSocket(port);
        statistics.clear();
        thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                Socket socketOut;
                try {
                    socketOut = serverSocket.accept();
                } catch (IOException ignored) {
                    logger.debug("IO error");
                    return;
                }
                Socket finalSocketOut = socketOut;
                pool.add(new Thread(() -> {
                    try (Socket socket = finalSocketOut) {
                        while (!Thread.currentThread().isInterrupted()) {
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
                        }
                    } catch (IOException ignored) {
                    }
                }));
                pool.get(pool.size() - 1).start();
            }
        });
        thread.start();
    }

    public void stop() throws IOException, InterruptedException {
        logger.debug("stop");
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
