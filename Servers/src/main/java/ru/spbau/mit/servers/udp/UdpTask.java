package ru.spbau.mit.servers.udp;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.ArrayProtos;
import ru.spbau.mit.algorithms.Sorts;
import ru.spbau.mit.servers.statistics.Statistic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;

import static ru.spbau.mit.Utility.packetToArrayMessage;

public class UdpTask implements Runnable {
    @org.jetbrains.annotations.NotNull
    private final static Logger logger = Logger.getLogger(UdpTask.class);
    @NotNull
    private final DatagramPacket packet;
    @NotNull
    private final DatagramSocket serverSocket;
    @NotNull
    private final List<Statistic> statistics;

    public UdpTask(@NotNull final DatagramPacket packet,
                   @NotNull final DatagramSocket serverSocket,
                   @NotNull final List<Statistic> statistics) {
        this.packet = packet;
        this.serverSocket = serverSocket;
        this.statistics = statistics;
    }

    @Override
    public void run() {
        long requestTime = System.nanoTime();

        ArrayProtos.ArrayMessage array;
        try {
            array = packetToArrayMessage(packet);
        } catch (InvalidProtocolBufferException e) {
            logger.warn("array invalid protocol buffer");
            return;
        }

        if (array == null) {
            logger.warn("array is null");
            return;
        }

        long processTime = System.nanoTime();
        final List<Integer> sortedArray = Sorts.bubbleSort(array.getValuesList());
        processTime = System.nanoTime() - processTime;

        final byte[] bytes = ArrayProtos.ArrayMessage.newBuilder().addAllValues(sortedArray).build().toByteArray();
        final DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, packet.getAddress(), packet.getPort());
        logger.debug("Send " + packet.getPort());
        try {
            serverSocket.send(sendPacket);
        } catch (IOException e) {
            logger.debug("Send error");
            return;
        }
        requestTime = System.nanoTime() - requestTime;
        statistics.add(new Statistic(requestTime, processTime));
    }
}
