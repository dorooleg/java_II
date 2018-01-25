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
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UdpSingleThreadOnClientServer implements IServer {
    private static final int MAX_SIZE = 65000;
    @NotNull
    private final List<Statistic> statistics = Collections.synchronizedList(new ArrayList<>());
    private final int port;
    private DatagramSocket serverSocket;
    private Thread thread;
    private List<Thread> pool = new ArrayList<>();

    public UdpSingleThreadOnClientServer(final int port) throws IOException {
        this.port = port;
    }

    static int bytesToInt(byte[] intBytes) {
        return ByteBuffer.wrap(intBytes).getInt();
    }

    public void start() throws IOException {
        serverSocket = new DatagramSocket(port);
        statistics.clear();
        thread = new Thread(() -> {
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

                System.out.println("Received " + packet.getPort());

                pool.add(new Thread(() -> {
                    long requestTime = System.nanoTime();
                    ArrayProtos.ArrayMessage array;


                    int receivedLength = bytesToInt(packet.getData());
                    byte[] receivedData = new byte[receivedLength];

                    System.arraycopy(packet.getData(), 4, receivedData, 0, receivedLength);

                    try {
                        array = ArrayProtos.ArrayMessage.parseFrom(receivedData);
                    } catch (InvalidProtocolBufferException e) {
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
                    System.out.println("Send " + packet.getPort());
                    try {
                        serverSocket.send(sendPacket);
                    } catch (IOException e) {
                        return;
                    }
                    requestTime = System.nanoTime() - requestTime;
                    statistics.add(new Statistic(requestTime, processTime));
                }));
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
