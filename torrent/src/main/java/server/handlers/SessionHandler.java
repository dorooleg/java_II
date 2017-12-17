package server.handlers;

import org.jetbrains.annotations.NotNull;
import server.Clients;
import server.tasks.SessionTask;
import shared.ISessionHandler;
import shared.Session;

import java.io.IOException;

public class SessionHandler implements ISessionHandler {
    private Clients clients;

    public SessionHandler(@NotNull final Clients clients) {
        this.clients = clients;
    }

    @Override
    public Runnable createSessionHandler(@NotNull Session session) throws IOException {
        return new SessionTask(session, clients);
    }
}
