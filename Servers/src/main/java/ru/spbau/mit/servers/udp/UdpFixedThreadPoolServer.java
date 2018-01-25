package ru.spbau.mit.servers.udp;

import com.google.protobuf.InvalidProtocolBufferException;
import com.sun.istack.internal.NotNull;
import ru.spbau.mit.ArrayProtos;
import ru.spbau.mit.algorithms.Sorts;
import ru.spbau.mit.servers.IServer;
import ru.spbau.mit.servers.statistics.Statistic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.spbau.mit.servers.udp.UdpSingleThreadOnClientServer.bytesToInt;

public class UdpFixedThreadPoolServer implements IServer {

    private static final int MAX_SIZE = 65000;
    @NotNull
    private final ExecutorService pool;
    private final List<Statistic> statistics = Collections.synchronizedList(new ArrayList<>());
    private final int port;
    private DatagramSocket serverSocket;

    public UdpFixedThreadPoolServer(final int port, int poolSize) throws IOException {
        this.port = port;
        pool = Executors.newFixedThreadPool(poolSize);
    }

    public void start() throws IOException {
        serverSocket = new DatagramSocket(port);
        statistics.clear();
        pool.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                byte[] msg = new byte[MAX_SIZE];
                DatagramPacket packet = new DatagramPacket(msg, msg.length);

                try {
                    serverSocket.receive(packet);
                } catch (IOException ignored) {
                }

                if (packet.getPort() < 0) {
                    continue;
                }

                long requestTime = System.nanoTime();

                int receivedLength = bytesToInt(packet.getData());
                byte[] receivedData = new byte[receivedLength];

                System.arraycopy(packet.getData(), 4, receivedData, 0, receivedLength);

                ArrayProtos.ArrayMessage array;

                try {
                    array = ArrayProtos.ArrayMessage.parseFrom(receivedData);
                } catch (InvalidProtocolBufferException e) {
                    System.out.println("Parse error");
                    return;
                }

                if (array == null) {
                    return;
                }

                long processTime = System.nanoTime();
                final List<Integer> sortedArray = Sorts.bubbleSort(array.getValuesList());
                processTime = System.nanoTime() - processTime;
                packet.getAddress();

                final byte[] bytes = ArrayProtos.ArrayMessage.newBuilder().addAllValues(sortedArray).build().toByteArray();
                DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, packet.getAddress(), packet.getPort());

                try {
                    serverSocket.send(sendPacket);
                } catch (IOException e) {
                    return;
                }
                requestTime = System.nanoTime() - requestTime;
                statistics.add(new Statistic(requestTime, processTime));
            }
        });
    }

    public void stop() throws IOException, InterruptedException {
        pool.shutdownNow();
        serverSocket.close();
    }

    @NotNull
    public List<Statistic> getStatistics() {
        return statistics;
    }
}
