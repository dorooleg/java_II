package server.handlers;

import org.jetbrains.annotations.NotNull;
import server.protocol.IServerProtocol;
import shared.protocol.messages.TypeMessage;
import shared.protocol.messages.request.ListRequestMessage;
import shared.protocol.messages.request.SourcesRequestMessage;
import shared.protocol.messages.request.UpdateRequestMessage;
import shared.protocol.messages.request.UploadRequestMessage;
import shared.protocol.messages.response.ListResponseMessage;
import shared.protocol.messages.response.SourcesResponseMessage;
import shared.protocol.messages.response.UpdateResponseMessage;
import shared.protocol.messages.response.UploadResponseMessage;

import java.io.IOException;

public class RequestHandler implements IRequestHandler {
    private final IServerProtocol protocol;
    private final IResponseHandler responseHandler;
    private long lastUpdate = 0;
    private short port = -1;

    public RequestHandler(@NotNull final IServerProtocol protocol, @NotNull final IResponseHandler responseHandler) {
        this.protocol = protocol;
        this.responseHandler = responseHandler;
    }

    private void processList() throws IOException {
        final ListRequestMessage requestMessage = protocol.receiveList();
        final ListResponseMessage responseMessage = responseHandler.getList(requestMessage);
        protocol.sendList(responseMessage);
    }

    private void processUpdate() throws IOException {
        lastUpdate = System.currentTimeMillis();
        final UpdateRequestMessage requestMessage = protocol.receiveUpdate();
        port = requestMessage.getPort();
        final UpdateResponseMessage responseMessage = responseHandler.getUpdate(requestMessage);
        protocol.sendUpdate(responseMessage);
    }

    private void processUpload() throws IOException {
        final UploadRequestMessage requestMessage = protocol.receiveUpload();
        final UploadResponseMessage responseMessage = responseHandler.getUpload(requestMessage);
        protocol.sendUpload(responseMessage);
    }

    private void processSources() throws IOException {
        final SourcesRequestMessage requestMessage = protocol.receiveSources();
        final SourcesResponseMessage responseMessage = responseHandler.getSources(requestMessage);
        protocol.sendSources(responseMessage);
    }

    public void processMessage() throws IOException {
        final TypeMessage type = protocol.receiveType();
        switch (type) {
            case LIST:
                processList();
                break;
            case UPDATE:
                processUpdate();
                break;
            case UPLOAD:
                processUpload();
                break;
            case SOURCES:
                processSources();
                break;
        }
    }

    public short getPort() {
        return port;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }


}
