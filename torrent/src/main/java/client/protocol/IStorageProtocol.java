package client.protocol;

import client.protocol.messages.request.GetRequestMessage;
import client.protocol.messages.request.StatRequestMessage;
import client.protocol.messages.response.GetResponseMessage;
import client.protocol.messages.response.StatResponseMessage;

import java.io.IOException;

public interface IStorageProtocol {

    GetResponseMessage receiveGet() throws IOException;

    StatResponseMessage receiveStat() throws IOException;

    void sendGet(GetRequestMessage message) throws IOException;

    void sendStat(StatRequestMessage message) throws IOException;
}
