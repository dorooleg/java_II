package shared.protocol.messages;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class FileInfo implements Serializable {
    private final int id;
    @NotNull
    private final String name;
    private final long size;

    public FileInfo(final int id, @NotNull final String name, final long size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }
}
