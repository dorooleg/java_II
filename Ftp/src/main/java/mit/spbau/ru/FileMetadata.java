package mit.spbau.ru;

import org.jetbrains.annotations.NotNull;

public class FileMetadata {

    private String name;
    private boolean isDirectory;

    public FileMetadata(@NotNull String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    @NotNull
    public String getName() {
        return name;
    }
}
