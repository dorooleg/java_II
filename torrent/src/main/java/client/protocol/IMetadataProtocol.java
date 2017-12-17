package client.protocol;

import shared.protocol.messages.request.ListRequestMessage;
import shared.protocol.messages.request.SourcesRequestMessage;
import shared.protocol.messages.request.UpdateRequestMessage;
import shared.protocol.messages.request.UploadRequestMessage;
import shared.protocol.messages.response.ListResponseMessage;
import shared.protocol.messages.response.SourcesResponseMessage;
import shared.protocol.messages.response.UpdateResponseMessage;
import shared.protocol.messages.response.UploadResponseMessage;

import java.io.IOException;

public interface IMetadataProtocol {

    UpdateResponseMessage receiveUpdate() throws IOException;

    UploadResponseMessage receiveUpload() throws IOException;

    ListResponseMessage receiveList() throws IOException;

    SourcesResponseMessage receiveSources() throws IOException;

    void sendUpdate(UpdateRequestMessage message) throws IOException;

    void sendUpload(UploadRequestMessage message) throws IOException;

    void sendList(ListRequestMessage message) throws IOException;

    void sendSources(SourcesRequestMessage message) throws IOException;
}
