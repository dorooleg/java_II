package shared.protocol.messages.response;

import org.jetbrains.annotations.NotNull;
import shared.protocol.messages.FileInfo;

import java.util.List;

public class ListResponseMessage {

    private final List<FileInfo> files;

    public ListResponseMessage(@NotNull final List<FileInfo> files) {
        this.files = files;
    }

    @NotNull
    public List<FileInfo> getFiles() {
        return files;
    }
}
