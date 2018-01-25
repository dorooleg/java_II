package ru.spbau.mit.protocol;

import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.ArrayProtos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TcpProtocol {
    @NotNull
    private final DataInputStream input;
    @NotNull
    private final DataOutputStream output;

    public TcpProtocol(@NotNull final DataInputStream input, @NotNull final DataOutputStream output) {
        this.input = input;
        this.output = output;
    }

    public ArrayProtos.ArrayMessage getArray() throws IOException {
        final int size = input.readInt();

        if (size == ArrayProtos.ArrayMessage.Type.DISCONNECT.getNumber()) {
            return null;
        }

        final byte[] buffer = new byte[size];
        input.readFully(buffer);
        return ArrayProtos.ArrayMessage.parseFrom(buffer);
    }

    public synchronized void sendArray(ArrayProtos.ArrayMessage array) throws IOException {
        if (array == null) {
            output.writeInt(ArrayProtos.ArrayMessage.Type.DISCONNECT.getNumber());
            return;
        }

        final byte[] buffer = array.toByteArray();
        output.writeInt(buffer.length);
        output.write(buffer);
    }

}
