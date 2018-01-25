package ru.spbau.mit;

import ru.spbau.mit.clients.ClientManager;
import ru.spbau.mit.clients.Clients;
import ru.spbau.mit.clients.ClientsPerRequest;
import ru.spbau.mit.clients.UdpClients;

import javax.swing.*;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ClientApplication {

    private static HashMap<String, ArrayList<Integer>> mapClient = new HashMap<>();
    private static HashMap<String, ArrayList<Integer>> mapRequest = new HashMap<>();
    private static HashMap<String, ArrayList<Integer>> mapResponse = new HashMap<>();

    public static void main(String[] args) throws IOException, InterruptedException {

        ClientManager manager = new ClientManager("localhost", 6660);
        manager.start();
/*
        System.out.println("X = 10");
        System.out.println("delta = 0");
        System.out.println("N = 1000");

        int X = 10;
        int delta = 0;
        //int N = 1000;
        int M = 4;
        for (int N = 10; N <= 1000; N += 100) {
            System.out.println("N = " + N);
            System.out.println("TCP_ASYNC");
            getStatistics(manager, ManagerProtos.StartMessage.Type.TCP_ASYNC, N, M, delta, X);
            System.out.println("TCP_NON_BLOCKING");
            getStatistics(manager, ManagerProtos.StartMessage.Type.TCP_NON_BLOCKING, N, M, delta, X);
            System.out.println("TCP_CACHED_THREAD");
            getStatistics(manager, ManagerProtos.StartMessage.Type.TCP_CACHED_THREAD, N, M, delta, X);
            System.out.println("TCP_SINGLE_THREAD");
            getStatistics(manager, ManagerProtos.StartMessage.Type.TCP_SINGLE_THREAD, N, M, delta, X);
            System.out.println("TCP_SINGLE_THREAD_ON_CLIENT");
            getStatistics(manager, ManagerProtos.StartMessage.Type.TCP_SINGLE_THREAD_ON_CLIENT, N, M, delta, X);
            System.out.println("UDP_SINGLE_THREAD_ON_REQUEST");
            getStatistics(manager, ManagerProtos.StartMessage.Type.UDP_SINGLE_THREAD_ON_REQUEST, N, M, delta, X);
            System.out.println("UDP_FIXED_POOL");
            getStatistics(manager, ManagerProtos.StartMessage.Type.UDP_FIXED_POOL, N, M, delta, X);

        }

        mapClient.entrySet().forEach(e -> {
            System.out.print(e.getKey() + " ");
            e.getValue().forEach(v -> System.out.print(v + " "));
            System.out.println();
        });

        mapRequest.entrySet().forEach(e -> {
            System.out.print(e.getKey() + " ");
            e.getValue().forEach(v -> System.out.print(v + " "));
            System.out.println();
        });

        mapResponse.entrySet().forEach(e -> {
            System.out.print(e.getKey() + " ");
            e.getValue().forEach(v -> System.out.print(v + " "));
            System.out.println();
        });
        */

        JFrame frame = new JFrame("Client");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(new JLabel("Client servers"));

        JButton button = new JButton();
        button.setText("Start");
        panel.add(button);

        panel.add(new JLabel("Architecture:"));

        final JList<? extends String> jList = new JList<>(new String[]{"ASYNC", "NON_BLOCKING", "CACHED_THREAD", "SINGLE_THREAD", "SINGLE_THREAD_ON_CLIENT", "UDP_FIXED_POOL", "UDP_SINGLE_THREAD_ON_REQUEST"});
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList.setSelectedIndex(0);
        panel.add(jList);

        panel.add(new JLabel("X:"));
        final JFormattedTextField X = new JFormattedTextField(NumberFormat.getIntegerInstance());
        panel.add(X);

        panel.add(new JLabel("N:"));
        final JFormattedTextField Nfrom = new JFormattedTextField(NumberFormat.getIntegerInstance());
        final JFormattedTextField Nto = new JFormattedTextField(NumberFormat.getIntegerInstance());
        final JFormattedTextField Nstep = new JFormattedTextField(NumberFormat.getIntegerInstance());

        panel.add(new JLabel("from:"));
        panel.add(Nfrom);
        panel.add(new JLabel("to:"));
        panel.add(Nto);
        panel.add(new JLabel("step:"));
        panel.add(Nstep);


        panel.add(new JLabel("M:"));
        final JFormattedTextField Mfrom = new JFormattedTextField(NumberFormat.getIntegerInstance());
        final JFormattedTextField Mto = new JFormattedTextField(NumberFormat.getIntegerInstance());
        final JFormattedTextField Mstep = new JFormattedTextField(NumberFormat.getIntegerInstance());

        panel.add(new JLabel("from:"));
        panel.add(Mfrom);
        panel.add(new JLabel("to:"));
        panel.add(Mto);
        panel.add(new JLabel("step:"));
        panel.add(Mstep);

        panel.add(new JLabel("Delta:"));
        final JFormattedTextField Deltafrom = new JFormattedTextField(NumberFormat.getIntegerInstance());
        final JFormattedTextField Deltato = new JFormattedTextField(NumberFormat.getIntegerInstance());
        final JFormattedTextField Deltastep = new JFormattedTextField(NumberFormat.getIntegerInstance());

        panel.add(new JLabel("from:"));
        panel.add(Deltafrom);
        panel.add(new JLabel("to:"));
        panel.add(Deltato);
        panel.add(new JLabel("step:"));
        panel.add(Deltastep);

        JTextArea textArea = new JTextArea(50, 10);
        panel.add(new JLabel("Result:"));
        panel.add(textArea);
        textArea.setEditable(false);

        frame.add(panel);
        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        button.addActionListener(e -> {

            ManagerProtos.StartMessage.Type type = ManagerProtos.StartMessage.Type.TCP_ASYNC;

            switch ((String)jList.getSelectedValue())
            {
                case "ASYNC":
                    type = ManagerProtos.StartMessage.Type.TCP_ASYNC;
                    break;
                case "NON_BLOCKING":
                    type = ManagerProtos.StartMessage.Type.TCP_NON_BLOCKING;
                    break;
                case "CACHED_THREAD":
                    type = ManagerProtos.StartMessage.Type.TCP_CACHED_THREAD;
                    break;
                case "SINGLE_THREAD":
                    type = ManagerProtos.StartMessage.Type.TCP_SINGLE_THREAD;
                    break;
                case "SINGLE_THREAD_ON_CLIENT":
                    type = ManagerProtos.StartMessage.Type.TCP_SINGLE_THREAD_ON_CLIENT;
                    break;
                case "UDP_FIXED_POOL":
                    type = ManagerProtos.StartMessage.Type.UDP_FIXED_POOL;
                    break;
                case "UDP_SINGLE_THREAD_ON_REQUEST":
                    type = ManagerProtos.StartMessage.Type.UDP_SINGLE_THREAD_ON_REQUEST;
                    break;
            }



            int NfromInt = Integer.parseInt(Nfrom.getText());
            int NtoInt = Integer.parseInt(Nto.getText());
            int NstepInt = Integer.parseInt(Nstep.getText());

            int MfromInt = Integer.parseInt(Mfrom.getText());
            int MtoInt = Integer.parseInt(Mto.getText());
            int MstepInt = Integer.parseInt(Mstep.getText());

            int DeltafromInt = Integer.parseInt(Deltafrom.getText());
            int DeltatoInt = Integer.parseInt(Deltato.getText());
            int DeltastepInt = Integer.parseInt(Deltastep.getText());

            int XInt = Integer.parseInt(X.getText());

            StringBuilder builder = new StringBuilder();

            for (; NfromInt <= NtoInt; NfromInt += NstepInt) {
                for (; MfromInt <= MtoInt; MfromInt += MstepInt) {
                    for (; DeltafromInt <= DeltatoInt; DeltafromInt += DeltastepInt) {
                        try {
                            getStatistics(manager, type, NfromInt, MfromInt, DeltafromInt, XInt);
                        } catch (IOException | InterruptedException ignored) {
                        }
                    }
                }
            }

        });

        manager.sendDisconnect();
    }

    private static void getStatistics(ClientManager manager, ManagerProtos.StartMessage.Type type, int N, int M, long delta, int X) throws IOException, InterruptedException {
        manager.sendStart(type);
        Thread.sleep(5000);
        long clientTime = 0;
        if (ManagerProtos.StartMessage.Type.TCP_SINGLE_THREAD == type) {
            ClientsPerRequest clients = new ClientsPerRequest("localhost", 6666, N, M, delta, X);
            clientTime = clients.run();
            System.out.println("Client average time: " + clientTime);
        } else if (ManagerProtos.StartMessage.Type.UDP_FIXED_POOL == type || ManagerProtos.StartMessage.Type.UDP_SINGLE_THREAD_ON_REQUEST == type) {
            UdpClients clients = new UdpClients("localhost", 6666, N, M, delta, X);
            clientTime = clients.run();
            System.out.println("Client average time: " + clientTime);
        } else {
            Clients clients = new Clients("localhost", 6666, N, M, delta, X);
            clientTime = clients.run();
            System.out.println("Client average time: " + clientTime);
        }

        manager.stop();

        final ManagerProtos.RequestMessage message = manager.getMessage();

        if (!message.hasResponse()) {
            System.err.println("Broken logic");
            manager.sendDisconnect();
            manager.stop();
            System.exit(-1);
        }

        System.out.println("Server average request time: " + message.getResponse().getAverageRequestTime());
        System.out.println("Server average response time: " + message.getResponse().getAverageResponseTime());

        mapClient.putIfAbsent(type.name(), new ArrayList<>());
        mapRequest.putIfAbsent(type.name(), new ArrayList<>());
        mapResponse.putIfAbsent(type.name(), new ArrayList<>());

        mapClient.get(type.name()).add((int) clientTime);
        mapRequest.get(type.name()).add((int)message.getResponse().getAverageRequestTime());
        mapResponse.get(type.name()).add((int)message.getResponse().getAverageResponseTime());
    }


}
