package mit.spbau.ru;

import org.apache.poi.util.IOUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Server implements IServer {

    private ServerSocket socket;
    private Thread mainThread;
    private int port;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public boolean start() {
        if (socket != null) {
            return false;
        }

        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            return false;
        }

        mainThread = new Thread(() -> {
            while (true) {
                try {
                    Socket client = socket.accept();

                    if (client == null) {
                        continue;
                    }


                                new Thread(() -> {
                                    while (true) {

                                        try {
                                            DataInputStream input = new DataInputStream(client.getInputStream());
                                            DataOutputStream out = new DataOutputStream(client.getOutputStream());

                                            int command = input.readInt();
                                            switch (command) {
                                                case 2: {
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
                                                break;
                                                case 1: {
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
                                            }
                                        } catch (IOException e) {
                                            if (client.isClosed()) {
                                                return;
                                            }
                                        }
                                    }

                                }).start();

                } catch (IOException e) {
                    if (socket.isClosed()) {
                        return;
                    }
                }
            }
        });
        mainThread.start();

        return true;
    }

    @Override
    public void stop() {
        if (socket == null) {
            return;
        }

        try {
            socket.close();
        } catch (IOException ignored) {
        } finally {

        }

        try {
            mainThread.join();
        } catch (InterruptedException ignored) {
        }

        socket = null;
    }
}
