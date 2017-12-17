package client.protocol.messages.request;

public class StatRequestMessage {

    private final int file;

    public StatRequestMessage(final int file) {
        this.file = file;
    }

    public int getFile() {
        return file;
    }
}
