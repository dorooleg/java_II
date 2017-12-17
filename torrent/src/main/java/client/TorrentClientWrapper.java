package client;

import client.server.handlers.SessionHandler;
import client.storage.IStorage;
import client.tasks.DownloadFileTask;
import client.tasks.DownloadStatus;
import client.tasks.StatusUpdaterTask;
import org.jetbrains.annotations.NotNull;
import shared.Server;
import shared.protocol.messages.ClientInfo;
import shared.protocol.messages.FileInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class TorrentClientWrapper {

    @NotNull
    private final String host;
    private final short port;
    private final short serverPort;
    private final IStorage storage;
    private MetadataClient client;
    private Server server;
    private StatusUpdaterTask updaterTask;

    public TorrentClientWrapper(@NotNull final IStorage storage, @NotNull final String host, final short port, final short serverPort) throws IOException, ClassNotFoundException {
        this.host = host;
        this.port = port;
        this.serverPort = serverPort;
        this.storage = storage;
    }

    public void start() throws IOException {
        client = new MetadataClient(host, port);
        server = new Server(serverPort, new SessionHandler(storage));
        updaterTask = new StatusUpdaterTask(serverPort, client, storage);
        client.start();
        server.start();
        updaterTask.start();
    }

    @NotNull
    public List<FileInfo> list() throws IOException {
        return client.list();
    }

    @NotNull
    public List<ClientInfo> sources(final int id) throws IOException {
        return client.sources(id);
    }

    @NotNull
    public Integer upload(@NotNull final String path, @NotNull final String name) throws IOException {
        final long size = Files.size(Paths.get(path));
        final Integer id = client.upload(name, size);
        storage.addFile(id, path, size);
        return id;
    }

    public void update() throws IOException {
        client.update(serverPort, storage.getFiles());
    }

    @NotNull
    public DownloadStatus download(@NotNull final String path, final int id) throws IOException, InterruptedException {
        final Optional<FileInfo> first = list().stream().filter(e -> e.getId() == id).findFirst();
        if (!first.isPresent()) {
            return DownloadStatus.FILE_NOT_FOUND;
        }

        try {
            storage.reserveFile(id, path, first.get().getSize());
        } catch (IllegalArgumentException ignored) {

        }

        return new DownloadFileTask(client, id, storage).execute();
    }

    public void stop() throws IOException {
        client.stop();
        server.stop();
        updaterTask.stop();
    }
}
