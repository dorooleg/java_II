package ru.spbau.mit.clients;

import com.google.protobuf.InvalidProtocolBufferException;
import ru.spbau.mit.ArrayProtos;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UdpClients {
    private String host;
    private int port;
    private int N;
    private int M;
    private long delta;
    private int X;

    public UdpClients(final String host, final int port, final int N, final int M, final long delta, final int X) {
        this.host = host;
        this.port = port;
        this.N = N;
        this.M = M;
        this.delta = delta;
        this.X = X;
    }

    public long run() {
        List<Thread> threads = new ArrayList<>();
        List<Long> times = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < M; i++) {
            threads.add(new Thread(() -> {
                DatagramSocket clientSocket;
                try {
                    clientSocket = new DatagramSocket();
                } catch (SocketException e) {
                    return;
                }

                for (int j = 0; j < X; j++) {
                    try {

                        final ArrayProtos.ArrayMessage array = ArrayProtos
                                .ArrayMessage
                                .newBuilder()
                                .addAllValues(new Random()
                                        .ints(N, 0, 1000)
                                        .boxed()
                                        .collect(Collectors.toList()))
                                .build();

                        byte[] sendData = new byte[65000];

                        System.arraycopy(ByteBuffer.allocate(4).putInt(array.toByteArray().length).array(), 0, sendData, 0, 4);
                        System.arraycopy(array.toByteArray(), 0, sendData, 4, array.toByteArray().length);

                        try {
                            ArrayProtos.ArrayMessage.parseFrom(array.toByteArray());
                        } catch (InvalidProtocolBufferException e) {
                            return;
                        }
                        final long clientTime = System.nanoTime();
                        DatagramPacket sendPacket = new DatagramPacket(sendData, 65000, InetAddress.getByName(host), port);
                        clientSocket.send(sendPacket);
                        byte[] data = new byte[65000];
                        DatagramPacket receivePacket = new DatagramPacket(data, data.length);

                        clientSocket.setSoTimeout(4000);
                        while (true) {
                            try {
                                clientSocket.receive(receivePacket);
                                break;
                            } catch (SocketTimeoutException ignored) {
                                clientSocket.send(sendPacket);
                            }
                        }

                        clientSocket.setSoTimeout(0);
                        times.add(System.nanoTime() - clientTime);
                        Thread.sleep(delta);
                    } catch (IOException | InterruptedException e) {
                        return;
                    }
                }
            }));
        }

        threads.forEach(Thread::start);

        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException ignored) {
            }
        });

        return (long) times.stream().mapToDouble(i -> i).average().orElse(0);
    }

}
