package mit.spbau.ru;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionAcceptor implements Runnable {

    private ServerSocket socket;

    public ConnectionAcceptor(ServerSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket client = socket.accept();

                if (client == null) {
                    continue;
                }

                new Thread(new ConnectionHandler(client)).start();

            } catch (IOException e) {
                if (socket.isClosed()) {
                    return;
                }
            }
        }
    }
}
