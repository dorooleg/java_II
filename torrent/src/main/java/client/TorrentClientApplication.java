package client;

import client.storage.Storage;
import org.jetbrains.annotations.NotNull;
import shared.protocol.messages.ClientInfo;
import shared.protocol.messages.FileInfo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TorrentClientApplication {

    private static final String LIST = "list";
    private static final String UPLOAD = "upload";
    private static final String SOURCES = "sources";
    private static final String DOWNLOAD = "download";
    private static final String UPDATE = "update";
    private static final String EXIT = "exit";
    private static final int EXIT_FAILURE = -1;

    private static void help() {
        System.out.println("Commands:");
        System.out.printf(" %s - list files %n", LIST);
        System.out.printf(" %s - upload file on tracker %n", UPLOAD);
        System.out.printf(" %s - list clients %n", SOURCES);
        System.out.printf(" %s - download file %n", DOWNLOAD);
        System.out.printf(" %s - exit %n", EXIT);
    }

    private static void list(@NotNull final TorrentClientWrapper client) throws IOException {
        for (@NotNull final FileInfo fileInfo : client.list()) {
            System.out.printf("%d %s %d %n", fileInfo.getId(), fileInfo.getName(), fileInfo.getSize());
        }
    }

    private static void upload(@NotNull final TorrentClientWrapper client, @NotNull final BufferedReader input) throws IOException {
        System.out.println("> Path to file: ");
        final String path = input.readLine();
        System.out.println("> File name: ");
        final String name = input.readLine();
        System.out.printf("%d %n", client.upload(path, name));
    }

    private static void sources(@NotNull final TorrentClientWrapper client, @NotNull final BufferedReader input) throws IOException {
        System.out.println("> Id file: ");
        int id;
        try {
            id = Integer.parseInt(input.readLine());
        } catch (NumberFormatException exception) {
            System.err.println("id must be integer");
            return;
        }

        for (ClientInfo info : client.sources(id)) {
            System.out.println(Utility.ipToString(info.getIp()) + " : " + info.getPort());
        }
    }

    private static void update(@NotNull final TorrentClientWrapper client) throws IOException {
        client.update();
    }

    private static void download(@NotNull final TorrentClientWrapper client, @NotNull final BufferedReader input) throws IOException, InterruptedException {
        System.out.println("> Id file: ");
        int id;
        try {
            id = Integer.parseInt(input.readLine());
        } catch (NumberFormatException exception) {
            System.err.println("id must be integer");
            return;
        }

        System.out.println("> Path file: ");

        final String path = input.readLine();

        System.out.print(client.download(path, id).toString());
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        final String host = "localhost";
        final short port = 6660;
        final short serverPort = 7770;

        final Storage storage;

        final Path storagePath = Paths
                .get(System.getProperty("java.io.tmpdir"))
                .resolve("client");

        if (Files.exists(storagePath)) {
            ObjectInputStream oi = new ObjectInputStream(new FileInputStream(storagePath.toFile()));
            storage = (Storage) oi.readObject();
            oi.close();
        } else {
            storage = new Storage();
        }

        TorrentClientWrapper client = null;

        try {
            client = new TorrentClientWrapper(storage, host, port, serverPort);
        } catch (IOException e) {
            System.out.println("Load storage problem");
            System.exit(EXIT_FAILURE);
        } catch (ClassNotFoundException e) {
            System.out.println("Storage class not found");
            System.exit(EXIT_FAILURE);
        }

        try {
            client.start();
        } catch (Exception e) {
            System.err.print(String.format("Startup client to server (%s:%d) broken", host, port));
            System.exit(EXIT_FAILURE);
        }

        final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        help();
        while (true) try {
            System.out.print("# ");
            final String command = input.readLine();

            switch (command) {
                case LIST:
                    list(client);
                    break;
                case UPLOAD:
                    upload(client, input);
                    break;
                case SOURCES:
                    sources(client, input);
                    break;
                case UPDATE:
                    update(client);
                case DOWNLOAD:
                    download(client, input);
                    break;
                case EXIT:
                    client.stop();
                    final ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(storagePath.toFile()));
                    oo.writeObject(storage);
                    System.out.println("Finished!");
                    System.exit(0);
                default:
                    help();
            }
        } catch (Exception | Error e) {
            e.printStackTrace();
        }
    }
}
