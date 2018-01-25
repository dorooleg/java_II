package ru.spbau.mit.clients;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.ArrayProtos;
import ru.spbau.mit.protocol.TcpProtocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TcpClients extends ClientsProvider {

    @NotNull
    private final static Logger logger = Logger.getLogger(TcpClients.class);

    public TcpClients(@NotNull final String host, int port, int N, int M, long delta, int X) {
        super(host, port, N, M, delta, X);
    }

    @Override
    protected Runnable createTask(@NotNull List<Long> times) {
        return () -> {
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
                    protocol.getArray();
                    times.add(System.nanoTime() - clientTime);
                    Thread.sleep(delta);
                } catch (IOException | InterruptedException e) {
                    logger.debug("IO | Interrupt");
                    return;
                }
            }
        };
    }
}
