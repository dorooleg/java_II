package server.handlers;

import shared.protocol.messages.request.ListRequestMessage;
import shared.protocol.messages.request.SourcesRequestMessage;
import shared.protocol.messages.request.UpdateRequestMessage;
import shared.protocol.messages.request.UploadRequestMessage;
import shared.protocol.messages.response.ListResponseMessage;
import shared.protocol.messages.response.SourcesResponseMessage;
import shared.protocol.messages.response.UpdateResponseMessage;
import shared.protocol.messages.response.UploadResponseMessage;

public interface IResponseHandler {
    UpdateResponseMessage getUpdate(UpdateRequestMessage message);

    UploadResponseMessage getUpload(UploadRequestMessage message);

    SourcesResponseMessage getSources(SourcesRequestMessage message);

    ListResponseMessage getList(ListRequestMessage message);
}
