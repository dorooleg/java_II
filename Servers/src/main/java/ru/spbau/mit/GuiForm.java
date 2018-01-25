package ru.spbau.mit;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.clients.ClientManager;
import ru.spbau.mit.clients.TcpClients;
import ru.spbau.mit.clients.TcpClientsPerRequest;
import ru.spbau.mit.clients.UdpClients;
import ru.spbau.mit.servers.statistics.Statistic;

import javax.swing.*;
import java.io.IOException;

public class GuiForm extends JFrame {

    private final static String host = "localhost";
    private final static int port = 6666;

    private final static Logger logger = Logger.getLogger(GuiForm.class);

    private Thread thread;

    public GuiForm(@NotNull final ClientManager manager) {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

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
        final JTextField X = new JTextField();
        panel.add(X);

        panel.add(new JLabel(" N: "));

        Box Nlayout = Box.createHorizontalBox();

        final JTextField Nfrom = new JTextField();
        final JTextField Nto = new JTextField();
        final JTextField Nstep = new JTextField();

        Nlayout.add(new JLabel(" from: "));
        Nlayout.add(Nfrom);
        Nlayout.add(new JLabel(" to: "));
        Nlayout.add(Nto);
        Nlayout.add(new JLabel(" step: "));
        Nlayout.add(Nstep);
        panel.add(Nlayout);

        panel.add(new JLabel(" M: "));

        Box Mlayout = Box.createHorizontalBox();

        final JTextField Mfrom = new JTextField();
        final JTextField Mto = new JTextField();
        final JTextField Mstep = new JTextField();

        Mlayout.add(new JLabel(" from: "));
        Mlayout.add(Mfrom);
        Mlayout.add(new JLabel(" to: "));
        Mlayout.add(Mto);
        Mlayout.add(new JLabel(" step: "));
        Mlayout.add(Mstep);
        panel.add(Mlayout);

        panel.add(new JLabel(" Delta: "));
        final JTextField Deltafrom = new JTextField();
        final JTextField Deltato = new JTextField();
        final JTextField Deltastep = new JTextField();

        Box Deltalayout = Box.createHorizontalBox();

        Deltalayout.add(new JLabel(" from: "));
        Deltalayout.add(Deltafrom);
        Deltalayout.add(new JLabel(" to: "));
        Deltalayout.add(Deltato);
        Deltalayout.add(new JLabel(" step: "));
        Deltalayout.add(Deltastep);
        panel.add(Deltalayout);

        JTextArea result = new JTextArea(50, 10);
        panel.add(new JLabel(" Result: "));
        JScrollPane scroll = new JScrollPane(result);
        panel.add(scroll);
        result.setEditable(false);

        button.addActionListener(e -> {

            if (thread != null) {
                thread.interrupt();
            }

            thread = new Thread(() -> {
                ManagerProtos.StartMessage.Type type = convertType(jList.getSelectedValue());

                try {
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

                    if (NfromInt < 0 || NtoInt < 0 || NstepInt <= 0
                            || MfromInt < 0 || MtoInt < 0 || MstepInt <= 0
                            || DeltafromInt < 0 || DeltatoInt < 0 || DeltastepInt <= 0 || XInt < 0) {
                        result.setText("Error invalid range");
                        return;
                    }


                    StringBuilder builder = new StringBuilder();

                    builder.append("Start:").append(System.lineSeparator());

                    result.setText(builder.toString());

                    for (; NfromInt <= NtoInt; NfromInt += NstepInt) {
                        for (; MfromInt <= MtoInt; MfromInt += MstepInt) {
                            for (; DeltafromInt <= DeltatoInt; DeltafromInt += DeltastepInt) {
                                Statistic statistic;
                                try {
                                    statistic = getStatistics(manager, type, NfromInt, MfromInt, DeltafromInt, XInt);
                                } catch (IOException | InterruptedException ignored) {
                                    logger.warn("statistics calculation error");
                                    continue;
                                }
                                builder
                                        .append(" type: ").append(type.name())
                                        .append(" N: ").append(NfromInt)
                                        .append(" M: ").append(MfromInt)
                                        .append(" Delta: ").append(DeltafromInt)
                                        .append(" X: ").append(XInt)
                                        .append(" ClientTime: ").append(statistic.getClientTime())
                                        .append(" RequestTime: ").append(statistic.getRequestTime())
                                        .append(" ProcessTime: ").append(statistic.getProcessTime())
                                        .append(System.lineSeparator());

                                result.setText(builder.toString());

                                if (Thread.interrupted()) {
                                    return;
                                }

                            }
                        }
                    }

                    builder.append("Done!");
                    result.setText(builder.toString());
                } catch (NumberFormatException el) {
                    result.setText("Error invalid parse");
                }

            });
            thread.start();
        });

        add(panel);
    }

    public static Statistic getStatistics(ClientManager manager, ManagerProtos.StartMessage.Type type, int N, int M, long delta, int X) throws IOException, InterruptedException {
        manager.sendStart(type);
        Thread.sleep(5000);

        long clientTime;
        if (ManagerProtos.StartMessage.Type.TCP_SINGLE_THREAD == type) {
            TcpClientsPerRequest clients = new TcpClientsPerRequest(host, port, N, M, delta, X);
            clientTime = clients.run();
        } else if (ManagerProtos.StartMessage.Type.UDP_FIXED_POOL == type || ManagerProtos.StartMessage.Type.UDP_SINGLE_THREAD_ON_REQUEST == type) {
            UdpClients clients = new UdpClients(host, port, N, M, delta, X);
            clientTime = clients.run();
        } else {
            TcpClients tcpClients = new TcpClients(host, port, N, M, delta, X);
            clientTime = tcpClients.run();
        }

        manager.sendStop();

        final ManagerProtos.RequestMessage message = manager.getMessage();

        if (!message.hasResponse()) {
            logger.error("Broken logic");
            manager.sendDisconnect();
            manager.stop();
            System.exit(-1);
        }

        return new Statistic(clientTime, (int) message.getResponse().getAverageRequestTime(), (int) message.getResponse().getAverageResponseTime());
    }

    private ManagerProtos.StartMessage.Type convertType(@NotNull final String stringType) {
        switch (stringType) {
            case "ASYNC":
                return ManagerProtos.StartMessage.Type.TCP_ASYNC;
            case "NON_BLOCKING":
                return ManagerProtos.StartMessage.Type.TCP_NON_BLOCKING;
            case "CACHED_THREAD":
                return ManagerProtos.StartMessage.Type.TCP_CACHED_THREAD;
            case "SINGLE_THREAD":
                return ManagerProtos.StartMessage.Type.TCP_SINGLE_THREAD;
            case "SINGLE_THREAD_ON_CLIENT":
                return ManagerProtos.StartMessage.Type.TCP_SINGLE_THREAD_ON_CLIENT;
            case "UDP_FIXED_POOL":
                return ManagerProtos.StartMessage.Type.UDP_FIXED_POOL;
            case "UDP_SINGLE_THREAD_ON_REQUEST":
                return ManagerProtos.StartMessage.Type.UDP_SINGLE_THREAD_ON_REQUEST;
        }
        return ManagerProtos.StartMessage.Type.TCP_ASYNC;
    }

    public void visible() {
        setSize(700, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
