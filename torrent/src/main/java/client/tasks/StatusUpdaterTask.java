package client.tasks;

import client.MetadataClient;
import client.storage.IStorage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class StatusUpdaterTask implements Runnable {

    private short port;
    @NotNull
    private final MetadataClient client;
    @NotNull
    private final IStorage storage;
    private Thread thread;

    public StatusUpdaterTask(final short port, @NotNull final MetadataClient client, @NotNull final IStorage storage) {
        this.port = port;
        this.client = client;
        this.storage = storage;
        thread = new Thread(this);
    }

    public void start() {
        thread.start();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                client.update(port, storage.getFiles());
            } catch (IOException ignored) {
            }

            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void stop() {
        thread.interrupt();
    }
}
