package mit.spbau.ru;

import org.apache.poi.util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ClientPlatform {
    public static void main(String[] args) {

        Client client = new Client("localhost", 5010);

        if (!client.connect()) {
            System.err.println("Cannot connect to server");
            return;
        }

        BufferedReader stdInReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("> ");
            String line = null;
            try {
                line = stdInReader.readLine();
            } catch (IOException e) {
                System.err.println("Can not read line from stdin");
                client.disconnect();
                return;
            }

            String[] split = line.split("\\s+");

            if (split.length != 2) {
                System.out.println("Invalid command");
                continue;
            }

            if (split[0].equals("ls")) {
                List<FileMetadata> listFiles = client.executeList(split[1]);
                if (listFiles == null) {
                    System.err.println("Can not get list files");
                    return;
                }

                for (FileMetadata data : listFiles) {
                    System.out.print(data.isDirectory() ? "d " : "f ");
                    System.out.println(data.getName());
                }
            } else if (split[0].equals("get")) {
                InputStream data = client.executeGet(split[1]);
                if (data == null) {
                    System.err.println("Can not get file");
                    return;
                }

                try {
                    IOUtils.copy(data, System.out);
                } catch (IOException e) {
                    System.err.println("Can not get file");
                    return;
                }
            } else if (split[0].equals("exit") && split[1].equals("now")) {
                client.disconnect();
                System.out.println("Client disconnect");
                return;
            } else {
                System.out.println("Invalid command");
            }
        }
    }
}
