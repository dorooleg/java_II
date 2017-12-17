package client.protocol.messages.response;

import org.jetbrains.annotations.NotNull;

public class GetResponseMessage {

    @NotNull
    private final byte[] bytes;

    public GetResponseMessage(@NotNull final byte[] bytes) {
        this.bytes = bytes;
    }

    @NotNull
    public byte[] getBytes() {
        return bytes;
    }
}
