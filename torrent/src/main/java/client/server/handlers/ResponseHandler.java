package client.server.handlers;

import client.protocol.messages.request.GetRequestMessage;
import client.protocol.messages.request.StatRequestMessage;
import client.protocol.messages.response.GetResponseMessage;
import client.protocol.messages.response.StatResponseMessage;
import client.storage.IStorage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Set;

public class ResponseHandler implements IResponseHandler {

    @NotNull
    private final IStorage storage;

    public ResponseHandler(@NotNull final IStorage storage) {
        this.storage = storage;
    }

    @Override
    public StatResponseMessage getStat(@NotNull final StatRequestMessage message) throws IOException {
        final Set<Integer> blocks = storage.getAvailableBlocks(message.getFile());
        return new StatResponseMessage(blocks);
    }

    @Override
    public GetResponseMessage getGet(@NotNull final GetRequestMessage message) throws IOException {
        final byte[] data = storage.readBlock(message.getFile(), message.getBlock());
        return new GetResponseMessage(data);
    }
}
