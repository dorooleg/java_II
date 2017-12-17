package shared;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface ISessionHandler {
    Runnable createSessionHandler(@NotNull final Session session) throws IOException;
}
