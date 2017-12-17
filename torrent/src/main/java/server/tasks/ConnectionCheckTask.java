package server.tasks;

import org.jetbrains.annotations.NotNull;
import server.Clients;
import server.handlers.IRequestHandler;
import shared.protocol.messages.ClientInfo;

import java.util.concurrent.TimeUnit;

public class ConnectionCheckTask implements Runnable {

    private final Clients clients;
    private final IRequestHandler requestHandler;
    private final Thread thread;
    private final byte[] ip;

    public ConnectionCheckTask(@NotNull final byte[] ip, @NotNull final Clients clients, @NotNull final IRequestHandler requestHandler) {
        this.ip = ip;
        this.clients = clients;
        this.requestHandler = requestHandler;
        thread = new Thread(this);
    }

    public void start() {
        thread.start();
    }

    public void stop() {
        thread.interrupt();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            final long start = System.currentTimeMillis();
            try {
                Thread.sleep(TimeUnit.MINUTES.toMillis(5));
            } catch (InterruptedException e) {
                break;
            }

            if (requestHandler.getLastUpdate() <= start) {
                if (requestHandler.getPort() >= 0) {
                    clients.remove(new ClientInfo(ip, requestHandler.getPort()));
                }
            }
        }
    }
}
