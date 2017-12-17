package server.handlers;

import org.jetbrains.annotations.NotNull;
import shared.protocol.messages.ClientInfo;
import server.Clients;
import shared.protocol.messages.request.ListRequestMessage;
import shared.protocol.messages.request.SourcesRequestMessage;
import shared.protocol.messages.request.UpdateRequestMessage;
import shared.protocol.messages.request.UploadRequestMessage;
import shared.protocol.messages.response.ListResponseMessage;
import shared.protocol.messages.response.SourcesResponseMessage;
import shared.protocol.messages.response.UpdateResponseMessage;
import shared.protocol.messages.response.UploadResponseMessage;

import java.util.ArrayList;
import java.util.List;

public class ResponseHandler implements IResponseHandler {

    private final byte[] ip;
    private final Clients clients;

    public ResponseHandler(@NotNull final byte[] ip, @NotNull final Clients clients) {
        this.ip = ip;
        this.clients = clients;
    }

    @NotNull
    @Override
    public UpdateResponseMessage getUpdate(@NotNull final UpdateRequestMessage message) {
        try {
            clients.update(new ClientInfo(ip, message.getPort()), message.getFiles());
            return new UpdateResponseMessage(true);
        } catch (Exception e) {
            return new UpdateResponseMessage(false);
        }
    }

    @NotNull
    @Override
    public UploadResponseMessage getUpload(@NotNull final UploadRequestMessage message) {
        final int id = clients.upload(message.getName(), message.getSize());
        return new UploadResponseMessage(id);
    }

    @NotNull
    @Override
    public SourcesResponseMessage getSources(@NotNull final SourcesRequestMessage message) {
        List<ClientInfo> clientsList;

        try {
            clientsList = clients.sources(message.getId());
        } catch (IllegalArgumentException ignored) {
            clientsList = new ArrayList<>();
        }

        return new SourcesResponseMessage(clientsList);
    }

    @NotNull
    @Override
    public ListResponseMessage getList(@NotNull final ListRequestMessage message) {
        return new ListResponseMessage(clients.list());
    }
}