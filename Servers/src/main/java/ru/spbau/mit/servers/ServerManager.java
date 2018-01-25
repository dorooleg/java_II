package ru.spbau.mit.servers;

import ru.spbau.mit.ManagerProtos;
import ru.spbau.mit.protocol.ManagerProtocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerManager {

    private final int port;
    private ServerSocket socket;
    private ManagerProtocol protocol;

    public ServerManager(final int port) {
        this.port = port;
    }

    public void start() throws IOException {
        socket = new ServerSocket(port);
    }

    public void accept() throws IOException {
        final Socket client = socket.accept();
        protocol = new ManagerProtocol(client.getInputStream(), client.getOutputStream());
    }

    public ManagerProtos.RequestMessage getMessage() throws IOException {
        return protocol.receiveRequest();
    }

    public void stop() throws IOException {
        socket.close();
    }

    public void sendResponse(final double averageRequestTime, final double averageResponseTime) throws IOException {
        protocol.sendResponse(averageRequestTime, averageResponseTime);
    }

}
