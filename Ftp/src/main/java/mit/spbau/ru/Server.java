package mit.spbau.ru;

import java.io.IOException;
import java.net.ServerSocket;

public class Server implements IServer {

    private ServerSocket socket;
    private Thread mainThread;
    private ConnectionAcceptor acceptor;
    private int port;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public boolean start() {
        if (socket != null) {
            return false;
        }

        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            return false;
        }

        acceptor = new ConnectionAcceptor(socket);
        mainThread = new Thread(acceptor);
        mainThread.start();

        return true;
    }

    @Override
    public void stop() {
        if (socket == null) {
            return;
        }

        try {
            socket.close();
        } catch (IOException ignored) {
        }

        try {
            mainThread.join();
        } catch (InterruptedException ignored) {
        }

        acceptor.stop();

        socket = null;
    }
}
