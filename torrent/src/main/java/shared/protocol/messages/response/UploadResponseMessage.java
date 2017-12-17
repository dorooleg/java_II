package shared.protocol.messages.response;

public class UploadResponseMessage {

    private final int id;

    public UploadResponseMessage(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
