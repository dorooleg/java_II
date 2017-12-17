package server.tasks;

import org.jetbrains.annotations.NotNull;
import server.Clients;
import server.handlers.IRequestHandler;
import server.handlers.IResponseHandler;
import server.handlers.RequestHandler;
import server.handlers.ResponseHandler;
import server.protocol.IServerProtocol;
import server.protocol.ServerProtocol;
import shared.Session;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SessionTask implements Runnable {

    private final ConnectionCheckTask checkTask;
    private final IRequestHandler requestHandler;
    private final Session session;

    public SessionTask(@NotNull final Session session, @NotNull final Clients clients) throws IOException {
        final DataInputStream input = new DataInputStream(session.getInputStream());
        final DataOutputStream output = new DataOutputStream(session.getOutputStream());
        final IServerProtocol protocol = new ServerProtocol(input, output);
        final IResponseHandler responseHandler = new ResponseHandler(session.getIp(), clients);
        requestHandler = new RequestHandler(protocol, responseHandler);
        checkTask = new ConnectionCheckTask(session.getIp(), clients, requestHandler);
        this.session = session;
    }

    @Override
    public void run() {
        checkTask.start();
        try {
            while (!Thread.interrupted()) {
                requestHandler.processMessage();
            }
        } catch (IOException ignored) {
        } finally {
            checkTask.stop();
            try {
                session.stop();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
