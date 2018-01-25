package ru.spbau.mit.servers;

import ru.spbau.mit.servers.statistics.Statistic;

import java.io.IOException;
import java.util.List;

public interface IServer {
    void start() throws IOException;

    void stop() throws InterruptedException, IOException;

    List<Statistic> getStatistics();
}
