package client.protocol;

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

public class MetadataProtocol implements IMetadataProtocol {

    @NotNull
    private final DataInputStream input;
    @NotNull
    private final DataOutputStream output;

    public MetadataProtocol(@NotNull final DataInputStream input, @NotNull final DataOutputStream output) {
        this.input = input;
        this.output = output;
    }

    @NotNull
    @Override
    public synchronized UpdateResponseMessage receiveUpdate() throws IOException {
        final boolean status = input.readBoolean();
        return new UpdateResponseMessage(status);
    }

    @NotNull
    @Override
    public synchronized UploadResponseMessage receiveUpload() throws IOException {
        final int id = input.readInt();
        return new UploadResponseMessage(id);
    }

    @NotNull
    @Override
    public synchronized ListResponseMessage receiveList() throws IOException {
        final int count = input.readInt();
        final List<FileInfo> files = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final int id = input.readInt();
            final String name = input.readUTF();
            final long size = input.readLong();
            files.add(new FileInfo(id, name, size));
        }
        return new ListResponseMessage(files);
    }

    @NotNull
    @Override
    public synchronized SourcesResponseMessage receiveSources() throws IOException {
        final int count = input.readInt();
        final List<ClientInfo> clients = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final byte[] ip = new byte[4];
            input.readFully(ip);
            final short port = input.readShort();
            clients.add(new ClientInfo(ip, port));
        }
        return new SourcesResponseMessage(clients);
    }

    @Override
    public synchronized void sendUpdate(@NotNull final UpdateRequestMessage message) throws IOException {
        output.writeByte(TypeMessage.UPDATE.getId());
        output.writeShort(message.getPort());
        output.writeInt(message.getFiles().size());
        for (final Integer file : message.getFiles()) {
            output.writeInt(file);
        }
    }

    @Override
    public synchronized void sendUpload(@NotNull final UploadRequestMessage message) throws IOException {
        output.writeByte(TypeMessage.UPLOAD.getId());
        output.writeUTF(message.getName());
        output.writeLong(message.getSize());
    }

    @Override
    public synchronized void sendList(@NotNull final ListRequestMessage message) throws IOException {
        output.writeByte(TypeMessage.LIST.getId());
    }

    @Override
    public synchronized void sendSources(@NotNull final SourcesRequestMessage message) throws IOException {
        output.writeByte(TypeMessage.SOURCES.getId());
        output.writeInt(message.getId());
    }
}
