package server.protocol;

import shared.protocol.messages.TypeMessage;
import shared.protocol.messages.request.*;
import shared.protocol.messages.response.ListResponseMessage;
import shared.protocol.messages.response.SourcesResponseMessage;
import shared.protocol.messages.response.UpdateResponseMessage;
import shared.protocol.messages.response.UploadResponseMessage;

import java.io.IOException;

public interface IServerProtocol {
    TypeMessage receiveType() throws IOException;

    UpdateRequestMessage receiveUpdate() throws IOException;

    UploadRequestMessage receiveUpload() throws IOException;

    ListRequestMessage receiveList() throws IOException;

    SourcesRequestMessage receiveSources() throws IOException;

    void sendUpdate(UpdateResponseMessage message) throws IOException;

    void sendUpload(UploadResponseMessage message) throws IOException;

    void sendList(ListResponseMessage message) throws IOException;

    void sendSources(SourcesResponseMessage message) throws IOException;
}
