package ru.spbau.mit.servers.tcp;

import com.sun.istack.internal.NotNull;
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
    private final List<Statistic> statistics = new ArrayList<>();
    private final int port;
    private ServerSocket serverSocket;
    private Thread thread;

    public SingleThreadServer(final int port) throws IOException {
        this.port = port;
    }

    public void start() throws IOException {
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
                        break;
                    }
                    long processTime = System.nanoTime();
                    final List<Integer> sortedArray = Sorts.bubbleSort(array.getValuesList());
                    processTime = System.nanoTime() - processTime;
                    protocol.sendArray(ArrayProtos.ArrayMessage.newBuilder().addAllValues(sortedArray).build());
                    requestTime = System.nanoTime() - requestTime;
                    statistics.add(new Statistic(requestTime, processTime));
                } catch (IOException ignored) {
                }
            }
        });

        thread.start();
    }

    public void stop() throws InterruptedException, IOException {
        thread.interrupt();
        serverSocket.close();
    }

    @NotNull
    public List<Statistic> getStatistics() {
        return statistics;
    }
}
