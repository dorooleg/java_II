package client.server.handlers;

import client.protocol.messages.TypeMessage;
import client.protocol.messages.request.GetRequestMessage;
import client.protocol.messages.request.StatRequestMessage;
import client.protocol.messages.response.GetResponseMessage;
import client.protocol.messages.response.StatResponseMessage;
import client.server.protocol.IServerProtocol;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class RequestHandler implements IRequestHandler {

    private final IServerProtocol protocol;
    private final IResponseHandler responseHandler;

    public RequestHandler(@NotNull final IServerProtocol protocol, @NotNull final IResponseHandler responseHandler) {
        this.protocol = protocol;
        this.responseHandler = responseHandler;
    }

    private void processGet() throws IOException {
        final GetRequestMessage requestMessage = protocol.receiveGet();
        final GetResponseMessage responseMessage = responseHandler.getGet(requestMessage);
        protocol.sendGet(responseMessage);
    }

    private void processStat() throws IOException {
        final StatRequestMessage requestMessage = protocol.receiveStat();
        final StatResponseMessage responseMessage = responseHandler.getStat(requestMessage);
        protocol.sendStat(responseMessage);
    }

    @Override
    public void processMessage() throws IOException {
        final TypeMessage type = protocol.receiveType();
        switch (type) {
            case GET:
                processGet();
                break;
            case STAT:
                processStat();
                break;
        }
    }

}
