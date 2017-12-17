package client.tasks;

import client.MetadataClient;
import client.StorageClient;
import client.storage.IStorage;
import org.jetbrains.annotations.NotNull;
import shared.protocol.messages.ClientInfo;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class DownloadFileTask {

    @NotNull
    final MetadataClient client;
    private final int file;
    private IStorage storage;


    public DownloadFileTask(@NotNull final MetadataClient client, final int file, @NotNull final IStorage storage) {
        this.client = client;
        this.file = file;
        this.storage = storage;
    }

    public DownloadStatus execute() throws IOException, InterruptedException {
        List<ClientInfo> sources = client.sources(file);

        if (sources.isEmpty()) {
            return DownloadStatus.SEEDS_NOT_FOUND;
        }

        Map<ClientInfo, StorageClient> clients = new HashMap<>();
        Map<ClientInfo, Set<Integer>> blocks = new HashMap<>();

        for (ClientInfo info : sources) {
            StorageClient storageClient = new StorageClient(info.getIp(), info.getPort());
            storageClient.start();
            blocks.put(info, storageClient.stat(file));
            clients.put(info, storageClient);
        }

        HashMap<ClientInfo, Set<Integer>> distributedBlocks = distributeBlocks(blocks);

        List<DownloadBlocksTask> blocksTasks = new ArrayList<>();

        for (Map.Entry<ClientInfo, Set<Integer>> clientBlocks : distributedBlocks.entrySet()) {
            DownloadBlocksTask downloadBlocksTask = new DownloadBlocksTask(clients.get(clientBlocks.getKey()), storage, file, clientBlocks.getValue());
            downloadBlocksTask.start();
            blocksTasks.add(downloadBlocksTask);
        }

        for (DownloadBlocksTask task : blocksTasks) {
            task.join();
        }

        for (StorageClient client : clients.values()) {
            client.stop();
        }

        return storage.getAvailableBlocks(file).size() == storage.getCountBlocks(file) ? DownloadStatus.FULL : DownloadStatus.PARTIAL;
    }

    private HashMap<ClientInfo, Set<Integer>> distributeBlocks(Map<ClientInfo, Set<Integer>> blocks) {
        final HashMap<ClientInfo, Set<Integer>> distributedBlocks = new HashMap<>();
        final Set<Integer> availableBlocks = storage.getAvailableBlocks(file);

        if (blocks.isEmpty()) {
            return distributedBlocks;
        }

        for (int i = 0; i < storage.getCountBlocks(file); i++) {
            if (availableBlocks.contains(i)) {
                continue;
            }

            final int finalI = i;

            Set<ClientInfo> collect = blocks
                    .entrySet()
                    .stream()
                    .filter(e -> e.getValue().contains(finalI))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());


            if (collect.isEmpty()) {
                continue;
            }

            Optional<ClientInfo> info = distributedBlocks
                    .entrySet()
                    .stream()
                    .filter(e -> collect.contains(e.getValue()))
                    .min(Comparator.comparingInt(a -> a.getValue().size())).map(Map.Entry::getKey);

            if (!info.isPresent()) {
                info = collect.stream().findFirst();
            }

            distributedBlocks.putIfAbsent(info.get(), new HashSet<>());
            distributedBlocks.get(info.get()).add(i);
        }

        return distributedBlocks;
    }


}
