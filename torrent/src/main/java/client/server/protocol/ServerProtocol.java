package client.server.protocol;

import client.protocol.messages.TypeMessage;
import client.protocol.messages.request.GetRequestMessage;
import client.protocol.messages.request.StatRequestMessage;
import client.protocol.messages.response.GetResponseMessage;
import client.protocol.messages.response.StatResponseMessage;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerProtocol implements IServerProtocol {

    @NotNull
    private final DataInputStream input;
    @NotNull
    private final DataOutputStream output;

    public ServerProtocol(@NotNull DataInputStream input, @NotNull DataOutputStream output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public TypeMessage receiveType() throws IOException {
        final int code = input.readByte();
        return TypeMessage.getEnum(code);
    }

    @Override
    public StatRequestMessage receiveStat() throws IOException {
        return new StatRequestMessage(input.readInt());
    }

    @Override
    public GetRequestMessage receiveGet() throws IOException {
        final int file = input.readInt();
        final int part = input.readInt();
        return new GetRequestMessage(file, part);
    }

    @Override
    public void sendStat(@NotNull final StatResponseMessage message) throws IOException {
        output.writeInt(message.getBlocks().size());
        for (final Integer part : message.getBlocks()) {
            output.writeInt(part);
        }
    }

    @Override
    public void sendGet(@NotNull final GetResponseMessage message) throws IOException {
        output.write(message.getBytes());
        output.flush();
    }
}
