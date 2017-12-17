package client.storage;

import client.Utility;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Set;

public class FileInfo implements IFileInfo, Serializable {
    public static final int BLOCK_SIZE = 10 * 1024 * 1024;
    @NotNull
    private final String path;
    private final long size;
    @NotNull
    private final Set<Integer> availableBlocks;

    public FileInfo(@NotNull String path, long size, @NotNull Set<Integer> availableBlocks) {

        if (size < 0) {
            throw new IllegalArgumentException("Invalid size = " + size + " for file " + path);
        }

        this.path = path;
        this.size = size;
        this.availableBlocks = availableBlocks;
    }

    public int getCountBlocks() {
        return Utility.countBlocks(size, BLOCK_SIZE);
    }

    private void validate(int blockId) {
        if (blockId < 0 || blockId >= getCountBlocks()) {
            throw new IllegalArgumentException("Block id = " + blockId + " invalid. Count blocks = " + getCountBlocks());
        }
    }

    @Override
    public boolean isBlockAvailable(int blockId) {
        validate(blockId);
        return availableBlocks.contains(blockId);
    }

    @Override
    public void addAvailableBlock(int blockId) {
        validate(blockId);
        availableBlocks.add(blockId);
    }

    @NotNull
    @Override
    public Set<Integer> getAvailableBlocks() {
        return availableBlocks;
    }

    @Override
    public int getBlockSize(int blockId) {
        validate(blockId);
        return (int) (blockId + 1 == getCountBlocks() ? (size - 1) % BLOCK_SIZE + 1 : BLOCK_SIZE);
    }

    @NotNull
    @Override
    public String getPath() {
        return path;
    }
}
