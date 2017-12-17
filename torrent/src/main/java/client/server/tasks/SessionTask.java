package client.server.tasks;

import client.server.handlers.IResponseHandler;
import client.server.handlers.RequestHandler;
import client.server.handlers.ResponseHandler;
import client.server.protocol.IServerProtocol;
import client.server.protocol.ServerProtocol;
import client.storage.IStorage;
import org.jetbrains.annotations.NotNull;
import shared.Session;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SessionTask implements Runnable {

    @NotNull
    private final RequestHandler requestHandler;
    @NotNull
    private final Session session;

    public SessionTask(@NotNull final Session session, @NotNull final IStorage storage) throws IOException {
        final DataInputStream input = new DataInputStream(session.getInputStream());
        final DataOutputStream output = new DataOutputStream(session.getOutputStream());
        final IServerProtocol protocol = new ServerProtocol(input, output);
        final IResponseHandler responseHandler = new ResponseHandler(storage);
        requestHandler = new RequestHandler(protocol, responseHandler);
        this.session = session;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                requestHandler.processMessage();
            }
        } catch (IOException ignored) {
        } finally {
            try {
                session.stop();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
