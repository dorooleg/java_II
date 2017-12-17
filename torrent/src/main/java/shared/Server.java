package shared;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    private final int port;
    private ServerSocket socket;
    private Accepter task;
    private final ISessionHandler sessionHandler;

    public Server(final int port, @NotNull final ISessionHandler sessionHandler) {
        this.port = port;
        this.sessionHandler = sessionHandler;
    }

    public void start() throws IOException {
        socket = new ServerSocket(port);
        task = new Accepter(socket, sessionHandler);
        task.start();
    }

    public void stop() throws IOException {
        socket.close();
        task.stop();
    }
}
