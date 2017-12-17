package shared;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class Accepter implements Runnable {
    private final ServerSocket socket;
    private final Set<Session> sessions;
    private final Thread thread;
    private final ISessionHandler sessionHandler;

    public Accepter(@NotNull final ServerSocket socket, @NotNull final ISessionHandler sessionHandler) {
        this.socket = socket;
        sessions = new ConcurrentSkipListSet<>();
        thread = new Thread(this);
        this.sessionHandler = sessionHandler;
    }

    public void start() {
        thread.start();
    }

    public void stop() throws IOException {
        thread.interrupt();
        for (final Session session : sessions) {
            session.stop();
        }
        sessions.clear();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            Session session;
            try {
                session = new Session(socket.accept(), sessions::remove, sessionHandler);
            } catch (IOException e) {
                continue;
            }

            sessions.add(session);
            session.start();
        }
    }
}
