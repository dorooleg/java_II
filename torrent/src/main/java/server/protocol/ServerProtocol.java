package server.protocol;

import org.jetbrains.annotations.NotNull;
import shared.protocol.messages.ClientInfo;
import shared.protocol.messages.FileInfo;
import shared.protocol.messages.TypeMessage;
import shared.protocol.messages.request.ListRequestMessage;
import shared.protocol.messages.request.SourcesRequestMessage;
import shared.protocol.messages.request.UpdateRequestMessage;
import shared.protocol.messages.request.UploadRequestMessage;
import shared.protocol.messages.response.ListResponseMessage;
import shared.protocol.messages.response.SourcesResponseMessage;
import shared.protocol.messages.response.UpdateResponseMessage;
import shared.protocol.messages.response.UploadResponseMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerProtocol implements IServerProtocol {
    @NotNull
    private final DataInputStream input;
    @NotNull
    private final DataOutputStream output;

    public ServerProtocol(@NotNull DataInputStream input, @NotNull DataOutputStream output) {
        this.input = input;
        this.output = output;
    }

    @NotNull
    @Override
    public TypeMessage receiveType() throws IOException {
        final int code = input.readByte();
        return TypeMessage.getEnum(code);
    }

    @NotNull
    @Override
    public UpdateRequestMessage receiveUpdate() throws IOException {
        final short port = input.readShort();
        final int count = input.readInt();
        final List<Integer> files = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            files.add(input.readInt());
        }
        return new UpdateRequestMessage(port, files);
    }

    @NotNull
    @Override
    public UploadRequestMessage receiveUpload() throws IOException {
        final String name = input.readUTF();
        final long size = input.readLong();
        return new UploadRequestMessage(name, size);
    }

    @NotNull
    @Override
    public ListRequestMessage receiveList() throws IOException {
        return new ListRequestMessage();
    }

    @NotNull
    @Override
    public SourcesRequestMessage receiveSources() throws IOException {
        final int id = input.readInt();
        return new SourcesRequestMessage(id);
    }

    @Override
    public void sendUpdate(@NotNull final UpdateResponseMessage message) throws IOException {
        output.writeBoolean(message.getStatus());
    }

    @Override
    public void sendUpload(@NotNull final UploadResponseMessage message) throws IOException {
        output.writeInt(message.getId());
    }

    @Override
    public void sendList(@NotNull ListResponseMessage message) throws IOException {
        output.writeInt(message.getFiles().size());
        for (final FileInfo info : message.getFiles()) {
            output.writeInt(info.getId());
            output.writeUTF(info.getName());
            output.writeLong(info.getSize());
        }
    }

    @Override
    public void sendSources(@NotNull final SourcesResponseMessage message) throws IOException {
        output.writeInt(message.getClients().size());
        for (final ClientInfo info : message.getClients()) {
            output.write(info.getIp());
            output.writeShort(info.getPort());
        }
    }

}
