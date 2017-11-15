package mit.spbau.ru;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.List;

public interface IClient {
    boolean connect();

    void disconnect();

    List<FileMetadata> executeList(@NotNull String path);

    InputStream executeGet(@NotNull String path);

}
