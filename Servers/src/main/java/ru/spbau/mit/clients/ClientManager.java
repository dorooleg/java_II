package ru.spbau.mit.clients;

import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.ManagerProtos;
import ru.spbau.mit.protocol.ManagerProtocol;

import java.io.IOException;
import java.net.Socket;

public class ClientManager {

    @NotNull
    private final String host;
    @NotNull
    private final int port;
    private Socket socket;
    private ManagerProtocol protocol;

    public ClientManager(@NotNull final String host, final int port) {
        this.host = host;
        this.port = port;
    }

    public void sendStart(@NotNull final ManagerProtos.StartMessage.Type type) throws IOException {
        protocol.sendStart(type);
    }

    public void sendStop() throws IOException {
        protocol.sendStop();
    }

    public void sendDisconnect() throws IOException {
        protocol.sendDisconnect();
    }

    public void start() throws IOException {
        socket = new Socket(host, port);
        protocol = new ManagerProtocol(socket.getInputStream(), socket.getOutputStream());
    }

    public void stop() throws IOException {
        socket.close();
    }

    public ManagerProtos.RequestMessage getMessage() throws IOException {
        return protocol.receiveRequest();
    }
}
