package client.protocol;

import client.protocol.messages.TypeMessage;
import client.protocol.messages.request.GetRequestMessage;
import client.protocol.messages.request.StatRequestMessage;
import client.protocol.messages.response.GetResponseMessage;
import client.protocol.messages.response.StatResponseMessage;
import client.storage.FileInfo;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class StorageProtocol implements IStorageProtocol {

    @NotNull
    private final DataInputStream input;
    @NotNull
    private final DataOutputStream output;

    public StorageProtocol(@NotNull final DataInputStream input, @NotNull final DataOutputStream output) {
        this.input = input;
        this.output = output;
    }

    @NotNull
    @Override
    public GetResponseMessage receiveGet() throws IOException {
        final byte[] buffer = new byte[FileInfo.BLOCK_SIZE];
        int size = 0;
        while (size != FileInfo.BLOCK_SIZE) {
            size += input.read(buffer, size, FileInfo.BLOCK_SIZE - size);
        }
        return new GetResponseMessage(buffer);
    }

    @NotNull
    @Override
    public StatResponseMessage receiveStat() throws IOException {
        final int count = input.readInt();
        final Set<Integer> blocks = new HashSet<>();
        for (int i = 0; i < count; i++) {
            blocks.add(input.readInt());
        }
        return new StatResponseMessage(blocks);
    }

    @Override
    public void sendGet(@NotNull final GetRequestMessage message) throws IOException {
        output.writeByte(TypeMessage.GET.getId());
        output.writeInt(message.getFile());
        output.writeInt(message.getBlock());
    }

    @Override
    public void sendStat(@NotNull final StatRequestMessage message) throws IOException {
        output.writeByte(TypeMessage.STAT.getId());
        output.writeInt(message.getFile());
    }
}
