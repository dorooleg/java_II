package ru.spbau.mit;

import com.google.protobuf.InvalidProtocolBufferException;
import org.jetbrains.annotations.NotNull;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

public class Utility {
    public static final int MAX_UDP_SIZE = 65000;

    public static int bytesToInt(@NotNull final byte[] intBytes) {
        return ByteBuffer.wrap(intBytes).getInt();
    }

    public static ArrayProtos.ArrayMessage packetToArrayMessage(@NotNull final DatagramPacket packet) throws InvalidProtocolBufferException {
        final int receivedLength = bytesToInt(packet.getData());
        final byte[] receivedData = new byte[receivedLength];

        System.arraycopy(packet.getData(), Integer.BYTES, receivedData, 0, receivedLength);

        return ArrayProtos.ArrayMessage.parseFrom(receivedData);
    }
}
