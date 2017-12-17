package client.storage;

import java.util.Set;

public interface IFileInfo {

    boolean isBlockAvailable(int blockId);

    void addAvailableBlock(int blockId);

    Set<Integer> getAvailableBlocks();

    int getBlockSize(int blockId);

    String getPath();

    int getCountBlocks();
}
