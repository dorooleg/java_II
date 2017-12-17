package client.storage;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IStorage {

    void addFile(int id, String path, final long size) throws IOException;

    void reserveFile(int id, String path, long size) throws IOException;

    byte[] readBlock(int id, int blockId) throws IOException;

    void writeBlock(int id, int blockId, byte[] buffer) throws IOException;

    String getPath(int id);

    int getCountBlocks(int id);

    Set<Integer> getAvailableBlocks(int id);

    List<Integer> getFiles();

    boolean isExists(int id);


}
