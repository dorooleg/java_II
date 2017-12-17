package shared;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class Session implements Comparable {
    private final static AtomicLong globalId = new AtomicLong();
    private final long id = globalId.incrementAndGet();
    private final Socket client;
    private final Thread thread;
    private final Consumer<Session> disposer;

    public Session(@NotNull final Socket client,
                   @NotNull final Consumer<Session> disposer,
                   @NotNull final ISessionHandler sessionHandler) throws IOException {
        this.client = client;
        this.disposer = disposer;

        final Runnable handler = sessionHandler.createSessionHandler(this);
        thread = new Thread(() -> {
            handler.run();
            try {
                stop();
            } catch (IOException ignored) {
            }
        });
    }

    public InputStream getInputStream() throws IOException {
        return client.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return client.getOutputStream();
    }

    public byte[] getIp() {
        return client.getInetAddress().getAddress();
    }

    public void start() {
        thread.start();
    }

    public void stop() throws IOException {
        client.close();
        disposer.accept(this);
        thread.interrupt();
    }

    @Override
    public int compareTo(@NotNull Object o) {
        final Session other = (Session) o;
        return Long.compare(id, other.id);
    }
}
