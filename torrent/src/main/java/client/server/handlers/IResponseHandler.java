package client.server.handlers;

import client.protocol.messages.request.GetRequestMessage;
import client.protocol.messages.request.StatRequestMessage;
import client.protocol.messages.response.GetResponseMessage;
import client.protocol.messages.response.StatResponseMessage;

import java.io.IOException;

public interface IResponseHandler {

    StatResponseMessage getStat(StatRequestMessage message) throws IOException;

    GetResponseMessage getGet(GetRequestMessage message) throws IOException;
}
