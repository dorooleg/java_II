package client.server.protocol;

import client.protocol.messages.TypeMessage;
import client.protocol.messages.request.GetRequestMessage;
import client.protocol.messages.request.StatRequestMessage;
import client.protocol.messages.response.GetResponseMessage;
import client.protocol.messages.response.StatResponseMessage;

import java.io.IOException;

public interface IServerProtocol {

    TypeMessage receiveType() throws IOException;

    StatRequestMessage receiveStat() throws IOException;

    GetRequestMessage receiveGet() throws IOException;

    void sendStat(StatResponseMessage message) throws IOException;

    void sendGet(GetResponseMessage message) throws IOException;
}
