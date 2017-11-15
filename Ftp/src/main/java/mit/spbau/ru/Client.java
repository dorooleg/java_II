package mit.spbau.ru;

import org.apache.poi.util.BoundedInputStream;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client implements IClient {

    private String hostName;
    private int port;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public Client(@NotNull String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }

    @Override
    public boolean connect() {
        if (socket != null) {
            return false;
        }

        try {
            socket = new Socket(hostName, port);
        } catch (IOException e) {
            return false;
        }

        try {
            input = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            return false;
        }

        try {
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    @Override
    public void disconnect() {
        if (socket == null) {
            return;
        }

        try {
            socket.close();
        } catch (IOException ignored) {
        } finally {
            socket = null;
        }
    }

    @Override
    public List<FileMetadata> executeList(@NotNull String path) {
        if (socket == null || output == null || input == null) {
            return null;
        }

        List<FileMetadata> files = new ArrayList<>();
        try {
            output.writeInt(Header.LIST.type());
            output.writeUTF(path);
            long countFiles = input.readLong();
            for (int i = 0; i < countFiles; i++) {
                files.add(new FileMetadata(input.readUTF(), input.readBoolean()));
            }
        } catch (IOException e) {
            return null;
        }

        return files;
    }

    @Override
    public InputStream executeGet(@NotNull String path) {
        try {
            output.writeInt(Header.GET.type());
            output.writeUTF(path);
            long fileSize = input.readLong();
            InputStream inputData = new BoundedInputStream(input, fileSize);
            return inputData;
        } catch (IOException e) {
            return null;
        }
    }
}
