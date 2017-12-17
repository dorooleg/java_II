package client.server.handlers;

import client.server.tasks.SessionTask;
import client.storage.IStorage;
import org.jetbrains.annotations.NotNull;
import shared.ISessionHandler;
import shared.Session;

import java.io.IOException;

public class SessionHandler implements ISessionHandler {

    @NotNull
    private IStorage storage;

    public SessionHandler(@NotNull final IStorage storage) {
        this.storage = storage;
    }

    @NotNull
    @Override
    public Runnable createSessionHandler(@NotNull final Session session) throws IOException {
        return new SessionTask(session, storage);
    }
}
