package client.storage;

import client.Utility;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Storage implements IStorage, Serializable {

    private final Map<Integer, FileInfo> files = new ConcurrentHashMap<>();

    private void validateFileExist(final int id) {
        if (!files.containsKey(id)) {
            throw new IllegalArgumentException("invalid id = " + id);
        }
    }

    private void validateFileNotExist(final int id) {
        if (files.containsKey(id)) {
            throw new IllegalArgumentException("invalid id = " + id);
        }
    }

    @Override
    public void addFile(final int id, @NotNull final String path, final long size) throws IOException {
        validateFileNotExist(id);
        final int count = Utility.countBlocks(size, FileInfo.BLOCK_SIZE);

        final Set<Integer> availableBlocks = new HashSet<>(IntStream.range(0, count).boxed().collect(Collectors.toSet()));

        files.put(id, new FileInfo(path, size, availableBlocks));
    }

    @Override
    public void reserveFile(final int id, @NotNull final String path, final long size) throws IOException {
        validateFileNotExist(id);

        if (size < 0) {
            throw new IllegalArgumentException("Invalid size = " + size + " for file " + path);
        }

        final RandomAccessFile f = new RandomAccessFile(path, "rw");
        f.setLength(size);
        f.close();
        files.put(id, new FileInfo(path, size, new HashSet<>()));
    }

    @NotNull
    @Override
    public byte[] readBlock(final int id, final int blockId) throws IOException {
        validateFileExist(id);
        final FileInfo info = files.get(id);

        if (!info.isBlockAvailable(blockId)) {
            throw new IllegalArgumentException("Block id = " + blockId + " not available");
        }

        final RandomAccessFile f = new RandomAccessFile(info.getPath(), "r");
        f.seek(FileInfo.BLOCK_SIZE * blockId);
        final byte[] buffer = new byte[FileInfo.BLOCK_SIZE];
        f.readFully(buffer, 0, info.getBlockSize(blockId));
        f.close();
        return buffer;
    }

    @Override
    public void writeBlock(final int id, final int blockId, @NotNull final byte[] buffer) throws IOException {
        validateFileExist(id);
        final FileInfo info = files.get(id);

        if (info.isBlockAvailable(blockId)) {
            throw new IllegalArgumentException("Block id = " + blockId + " already available");
        }

        if (buffer.length != FileInfo.BLOCK_SIZE) {
            throw new IllegalArgumentException("Block size = " + buffer.length + " for " + blockId + " invalid");
        }

        final RandomAccessFile f = new RandomAccessFile(info.getPath(), "rw");
        f.seek(FileInfo.BLOCK_SIZE * blockId);
        f.write(buffer, 0, info.getBlockSize(blockId));
        f.close();

        info.addAvailableBlock(blockId);
    }

    @NotNull
    @Override
    public String getPath(final int id) {
        validateFileExist(id);
        return files.get(id).getPath();
    }

    @Override
    public int getCountBlocks(final int id) {
        validateFileExist(id);
        return files.get(id).getCountBlocks();
    }

    @NotNull
    @Override
    public Set<Integer> getAvailableBlocks(final int id) {
        validateFileExist(id);
        return files.get(id).getAvailableBlocks();
    }

    @NotNull
    @Override
    public List<Integer> getFiles() {
        return new ArrayList<>(files.keySet());
    }

    @Override
    public boolean isExists(final int id) {
        return files.containsKey(id);
    }
}
