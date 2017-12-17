package client;

import client.protocol.IMetadataProtocol;
import client.protocol.MetadataProtocol;
import org.jetbrains.annotations.NotNull;
import shared.protocol.messages.ClientInfo;
import shared.protocol.messages.FileInfo;
import shared.protocol.messages.request.ListRequestMessage;
import shared.protocol.messages.request.SourcesRequestMessage;
import shared.protocol.messages.request.UpdateRequestMessage;
import shared.protocol.messages.request.UploadRequestMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class MetadataClient {

    @NotNull
    private final String host;
    private final int port;
    private Socket socket;
    private IMetadataProtocol protocol;

    public MetadataClient(@NotNull final String host, final int port) {
        this.host = host;
        this.port = port;
    }

    public List<FileInfo> list() throws IOException {
        protocol.sendList(new ListRequestMessage());
        return protocol.receiveList().getFiles();
    }

    public Integer upload(@NotNull final String name, final long size) throws IOException {
        protocol.sendUpload(new UploadRequestMessage(name, size));
        return protocol.receiveUpload().getId();
    }

    public List<ClientInfo> sources(@NotNull final Integer id) throws IOException {
        protocol.sendSources(new SourcesRequestMessage(id));
        return protocol.receiveSources().getClients();
    }

    public boolean update(final short port, @NotNull final List<Integer> files) throws IOException {
        protocol.sendUpdate(new UpdateRequestMessage(port, files));
        return protocol.receiveUpdate().getStatus();
    }

    public void start() throws IOException {
        socket = new Socket(host, port);
        protocol = new MetadataProtocol(new DataInputStream(socket.getInputStream()), new DataOutputStream(socket.getOutputStream()));
    }

    public void stop() throws IOException {
        socket.close();
    }
}
