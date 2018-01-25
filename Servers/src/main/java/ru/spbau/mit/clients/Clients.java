package ru.spbau.mit.clients;

import ru.spbau.mit.ArrayProtos;
import ru.spbau.mit.protocol.TcpProtocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Clients {
    private String host;
    private int port;
    private int N;
    private int M;
    private long delta;
    private int X;

    public Clients(final String host, final int port, final int N, final int M, final long delta, final int X) {
        this.host = host;
        this.port = port;
        this.N = N;
        this.M = M;
        this.delta = delta;
        this.X = X;
    }

    public long run() throws IOException {
        List<Thread> threads = new ArrayList<>();
        List<Long> times = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < M; i++) {
            threads.add(new Thread(() -> {
                final Socket socket;
                try {
                    socket = new Socket(host, port);
                } catch (IOException e) {
                    return;
                }
                for (int j = 0; j < X; j++) {
                    try {
                        final DataInputStream input = new DataInputStream(socket.getInputStream());
                        final DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                        TcpProtocol protocol = new TcpProtocol(input, output);
                        final ArrayProtos.ArrayMessage array = ArrayProtos
                                .ArrayMessage
                                .newBuilder()
                                .addAllValues(new Random()
                                        .ints(N, 0, 1000)
                                        .boxed()
                                        .collect(Collectors.toList()))
                                .build();


                        final long clientTime = System.nanoTime();
                        protocol.sendArray(array);

                        final ArrayProtos.ArrayMessage array1 = protocol.getArray();

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
