package server;

import client.storage.Storage;
import org.apache.commons.io.FileUtils;
import server.handlers.SessionHandler;
import shared.Server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TorrentServerApplication {
    private final static String EXIT = "exit";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        final Path clientsPath = Paths
                                    .get(System.getProperty("java.io.tmpdir"))
                                    .resolve("server");

        final Clients clients;

        if (Files.exists(clientsPath)) {
            ObjectInputStream oi = new ObjectInputStream(new FileInputStream(clientsPath.toFile()));
            clients = (Clients) oi.readObject();
            oi.close();
        } else {
            clients = new Clients();
        }

        final SessionHandler handler = new SessionHandler(clients);
        final Server server = new Server(6660, handler);
        server.start();
        final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String command;
        do {
            System.out.print("# ");
            command = input.readLine();
        } while (command != null && !command.equals(EXIT));
        server.stop();

        final ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(clientsPath.toFile()));
        oo.writeObject(clients);
    }
}