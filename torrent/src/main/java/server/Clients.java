package server;

import org.jetbrains.annotations.NotNull;
import shared.protocol.messages.ClientInfo;
import shared.protocol.messages.FileInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Clients implements Serializable {

    private final Map<Integer, Set<ClientInfo>> seeds;
    private final Map<Integer, FileInfo> files;
    private Integer counter = 0;

    public Clients() {
        files = new ConcurrentHashMap<>();
        seeds = new ConcurrentHashMap<>();
    }

    public void remove(@NotNull final ClientInfo info) {
        for (final Map.Entry<Integer, Set<ClientInfo>> id : seeds.entrySet()) {
            id.getValue().remove(info);
        }
    }

    @NotNull
    public List<FileInfo> list() {
        return new ArrayList<>(files.values());
    }

    @NotNull
    public List<ClientInfo> sources(final int id) {
        if (!files.containsKey(id)) {
            throw new IllegalArgumentException("File with id = " + id + " not exist");
        }

        return new ArrayList<>(seeds.get(id));
    }

    public int upload(@NotNull final String name, final long size) {
        int id;
        synchronized (this) {
            id = ++counter;
        }
        if (files.containsKey(id)) {
            throw new UnsupportedOperationException("File with id = " + id + " already exist");
        }
        files.put(id, new FileInfo(id, name, size));
        seeds.put(id, new ConcurrentSkipListSet<>());
        return id;
    }

    public void update(@NotNull final ClientInfo info, @NotNull final List<Integer> files) {
        for (final Integer id : files) {
            if (!this.files.containsKey(id)) {
                throw new IllegalArgumentException("File with id = " + id + " not exist");
            }
            seeds.getOrDefault(id, new ConcurrentSkipListSet<>()).add(info);
        }

        Set<Integer> collect = seeds.entrySet()
                                        .stream()
                                        .filter(e -> e.getValue().contains(info))
                                        .map(Map.Entry::getKey)
                                        .collect(Collectors.toSet());

        for (Integer file : collect) {
            if (!files.contains(file)) {
                seeds.get(file).remove(info);
            }
        }
    }
}
