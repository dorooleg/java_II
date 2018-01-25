package ru.spbau.mit.clients;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.ArrayProtos;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static ru.spbau.mit.Utility.MAX_UDP_SIZE;

public class UdpClients extends ClientsProvider {

    @NotNull
    private final static Logger logger = Logger.getLogger(UdpClients.class);

    public UdpClients(@NotNull final String host, int port, int N, int M, long delta, int X) {
        super(host, port, N, M, delta, X);
    }

    @Override
    protected Runnable createTask(@NotNull final List<Long> times) {
        return () -> {
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

                    byte[] sendData = new byte[MAX_UDP_SIZE];

                    System.arraycopy(ByteBuffer.allocate(Integer.BYTES).putInt(array.toByteArray().length).array(), 0, sendData, 0, 4);
                    System.arraycopy(array.toByteArray(), 0, sendData, Integer.BYTES, array.toByteArray().length);

                    try {
                        ArrayProtos.ArrayMessage.parseFrom(array.toByteArray());
                    } catch (InvalidProtocolBufferException e) {
                        logger.warn("Parse array invalid");
                        return;
                    }

                    final long clientTime = System.nanoTime();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, MAX_UDP_SIZE, InetAddress.getByName(host), port);
                    clientSocket.send(sendPacket);


                    clientSocket.setSoTimeout(4000);
                    DatagramPacket receivePacket = new DatagramPacket(new byte[MAX_UDP_SIZE], MAX_UDP_SIZE);
                    while (true) {
                        try {
                            clientSocket.receive(receivePacket);
                            break;
                        } catch (SocketTimeoutException ignored) {
                            clientSocket.send(sendPacket);
                            logger.warn("Udp message lost");
                        }
                    }
                    clientSocket.setSoTimeout(0);
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
