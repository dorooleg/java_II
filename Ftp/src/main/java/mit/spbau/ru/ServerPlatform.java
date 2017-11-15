package mit.spbau.ru;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerPlatform {

    public static void main(String[] args) {
        System.out.println("Server run with port 5010");
        Server server = new Server(5010);
        server.start();

        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in))) {
            buffer.read();
        } catch (IOException e) {
            System.err.println("Read from console error");
        } finally {
            server.stop();
        }

        System.out.println("Server stop");
    }
}
