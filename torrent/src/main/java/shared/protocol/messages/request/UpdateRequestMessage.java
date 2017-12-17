package shared.protocol.messages.request;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UpdateRequestMessage {

    private final short port;
    @NotNull
    private final List<Integer> files;

    public UpdateRequestMessage(final short port, @NotNull final List<Integer> files) {
        this.port = port;
        this.files = files;
    }

    public short getPort() {
        return port;
    }

    @NotNull
    public List<Integer> getFiles() {
        return files;
    }
}
