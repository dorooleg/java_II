package ru.spbau.mit.servers;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.ManagerProtos;
import ru.spbau.mit.protocol.ManagerProtocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerManager {

    @NotNull
    private final static Logger logger = Logger.getLogger(ServerManager.class);
    private final int port;
    private ServerSocket socket;
    private ManagerProtocol protocol;

    public ServerManager(final int port) {
        logger.debug("create");
        this.port = port;
    }

    public void start() throws IOException {
        logger.debug("start");
        socket = new ServerSocket(port);
    }

    public void accept() throws IOException {
        logger.debug("accept");
        final Socket client = socket.accept();
        protocol = new ManagerProtocol(client.getInputStream(), client.getOutputStream());
    }

    public ManagerProtos.RequestMessage getMessage() throws IOException {
        return protocol.receiveRequest();
    }

    public void stop() throws IOException {
        logger.debug("stop");
        socket.close();
    }

    public void sendResponse(final double averageRequestTime, final double averageResponseTime) throws IOException {
        protocol.sendResponse(averageRequestTime, averageResponseTime);
    }

}
