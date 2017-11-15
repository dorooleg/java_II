package mit.spbau.ru;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class ConnectionAcceptor implements Runnable {

    private final LinkedList<Socket> clients = new LinkedList<>();
    private ServerSocket socket;

    public ConnectionAcceptor(@NotNull ServerSocket socket) {
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

                synchronized (clients) {
                    clients.add(client);
                }

                new Thread(new ConnectionHandler(client)).start();

            } catch (IOException e) {
                if (socket.isClosed()) {
                    return;
                }
            }
        }
    }

    public void stop() {
        synchronized (clients) {
            for (Socket socket : clients) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
            clients.clear();
        }
    }
}
