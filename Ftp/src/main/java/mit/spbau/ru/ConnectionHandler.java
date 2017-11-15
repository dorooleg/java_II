package mit.spbau.ru;

import org.apache.poi.util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ConnectionHandler implements Runnable {

    private Socket socket;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;

    }

    @Override
    public void run() {
        while (true) {

            try {
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                int command = input.readInt();

                if (Header.GET.type() == command) {
                    File file = null;
                    try {
                        file = new File(input.readUTF());
                    } catch (Exception ex) {
                        out.writeLong(0);
                        continue;
                    }
                    try (BufferedInputStream fileStream = new BufferedInputStream(new FileInputStream(file))) {
                        long fileSize = file.length();
                        out.writeLong(fileSize);
                        IOUtils.copy(fileStream, out);
                    } catch (Exception ex) {
                        out.writeLong(0);
                    }
                }

                if (Header.LIST.type() == command) {
                    List<Path> paths;
                    try {
                        Path dir = Paths.get(input.readUTF());
                        try {
                            paths = Files.list(dir).collect(Collectors.toList());
                        } catch (FileNotFoundException | NotDirectoryException e) {
                            return;
                        }
                    } catch (Exception ex) {
                        out.writeLong(0);
                        continue;
                    }

                    out.writeLong(paths.size());
                    for (Path p : paths) {
                        out.writeUTF(p.toString());
                        out.writeBoolean(Files.isDirectory(p));
                    }
                }

            } catch (IOException e) {
                if (socket.isClosed()) {
                    return;
                }
            }
        }
    }
}
