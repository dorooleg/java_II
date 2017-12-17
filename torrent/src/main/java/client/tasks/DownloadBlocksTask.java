package client.tasks;

import client.StorageClient;
import client.storage.IStorage;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class DownloadBlocksTask implements Runnable {

    @NotNull
    private final StorageClient client;
    private IStorage storage;
    private final int file;
    private final Set<Integer> blocks;
    private final Thread thread;

    public DownloadBlocksTask(@NotNull final StorageClient client, @NotNull final IStorage storage, final int file, final Set<Integer> blocks) {
        this.client = client;
        this.storage = storage;
        this.file = file;
        this.blocks = blocks;
        thread = new Thread(this);
    }

    public void start() {
        thread.start();
    }

    public void stop() {
        thread.interrupt();
    }

    public void join() throws InterruptedException {
        thread.join();
    }

    @Override
    public void run() {
        try {
            for (Integer block : blocks) {
                storage.writeBlock(file, block, client.get(file, block));
            }
        } catch (Exception ignored) {
        }
    }
}
