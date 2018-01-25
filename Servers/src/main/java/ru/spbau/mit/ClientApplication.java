package ru.spbau.mit;

import ru.spbau.mit.clients.ClientManager;

import java.io.IOException;

public class ClientApplication {

    public static void main(String[] args) throws IOException, InterruptedException {

        ClientManager manager = new ClientManager("localhost", 6660);
        manager.start();

        GuiForm form = new GuiForm(manager);

        form.visible();

        // manager.sendDisconnect();
    }
}
