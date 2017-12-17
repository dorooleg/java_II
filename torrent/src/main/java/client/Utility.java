package client;

import org.jetbrains.annotations.NotNull;

public class Utility {
    public static int countBlocks(final long size, final int blockSize) {
        return (int) (size / blockSize + (size % blockSize == 0L ? 0L : 1L));
    }

    public static String ipToString(@NotNull final byte[] ip) {
        StringBuilder str = new StringBuilder();
        str.append(ip[0] & 0xFF);
        str.append(".");
        str.append(ip[1] & 0xFF);
        str.append(".");
        str.append(ip[2] & 0xFF);
        str.append(".");
        str.append(ip[3] & 0xFF);
        return str.toString();
    }
}
