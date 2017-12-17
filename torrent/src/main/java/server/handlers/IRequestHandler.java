package server.handlers;

import java.io.IOException;

public interface IRequestHandler {
    short getPort();

    long getLastUpdate();

    void processMessage() throws IOException;
}
