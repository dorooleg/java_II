package shared.protocol.messages.response;

public class UpdateResponseMessage {

    private final boolean status;

    public UpdateResponseMessage(final boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }
}
