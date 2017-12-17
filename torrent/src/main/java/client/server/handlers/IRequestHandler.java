package client.server.handlers;

import java.io.IOException;

public interface IRequestHandler {
    void processMessage() throws IOException;
}
