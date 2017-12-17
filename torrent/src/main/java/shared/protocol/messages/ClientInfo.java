package shared.protocol.messages;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class ClientInfo implements Comparable, Serializable {
    private final byte[] ip;
    private final short port;

    public ClientInfo(@NotNull final byte[] ip, final short port) {
        this.ip = ip;
        this.port = port;
    }

    @NotNull
    public byte[] getIp() {
        return ip;
    }

    public short getPort() {
        return port;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        ClientInfo info = (ClientInfo) o;
        for (int i = 0; i < ip.length; i++) {
            int result = Integer.compare(ip[i], info.ip[i]);
            if (result != 0) {
                return result;
            }
        }

        return Integer.compare(port, info.port);
    }
}
