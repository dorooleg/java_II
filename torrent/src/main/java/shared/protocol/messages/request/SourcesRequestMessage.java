package shared.protocol.messages.request;

public class SourcesRequestMessage {
    private final int id;

    public SourcesRequestMessage(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
