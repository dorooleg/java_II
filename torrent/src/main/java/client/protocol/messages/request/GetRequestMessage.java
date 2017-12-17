package client.protocol.messages.request;

public class GetRequestMessage {

    private final int file;
    private final int block;

    public GetRequestMessage(final int file, final int block) {
        this.file = file;
        this.block = block;
    }

    public int getFile() {
        return file;
    }

    public int getBlock() {
        return block;
    }
}
