package mit.spbau.ru;

import java.io.IOException;
import java.net.ServerSocket;

public class Server implements IServer {

    private ServerSocket socket;
    private Thread mainThread;
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

        mainThread = new Thread(new ConnectionAcceptor(socket));
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
        } finally {

        }

        try {
            mainThread.join();
        } catch (InterruptedException ignored) {
        }

        socket = null;
    }
}
