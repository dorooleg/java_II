package client;

import client.protocol.StorageProtocol;
import client.protocol.messages.request.GetRequestMessage;
import client.protocol.messages.request.StatRequestMessage;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Set;

public class StorageClient {

    @NotNull
    private final byte[] ip;
    private final short port;
    private Socket socket;
    private StorageProtocol protocol;

    public StorageClient(@NotNull final byte[] ip, final short port) {
        this.ip = ip;
        this.port = port;
    }

    public void start() throws IOException {
        socket = new Socket(Utility.ipToString(ip), port);
        protocol = new StorageProtocol(new DataInputStream(socket.getInputStream()), new DataOutputStream(socket.getOutputStream()));
    }

    @NotNull
    public Set<Integer> stat(final int file) throws IOException {
        protocol.sendStat(new StatRequestMessage(file));
        return protocol.receiveStat().getBlocks();
    }

    @NotNull
    public byte[] get(final int file, final int block) throws IOException {
        protocol.sendGet(new GetRequestMessage(file, block));
        return protocol.receiveGet().getBytes();
    }

    public void stop() throws IOException {
        socket.close();
    }
}
