package shared.protocol.messages.request;

import org.jetbrains.annotations.NotNull;

public class UploadRequestMessage {

    @NotNull
    private final String name;
    private final long size;

    public UploadRequestMessage(@NotNull final String name, final long size) {
        this.name = name;
        this.size = size;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }
}
