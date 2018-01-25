package ru.spbau.mit.clients;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ClientsProvider {

    @NotNull
    protected String host;
    protected int port;
    protected int N;
    protected int M;
    protected long delta;
    protected int X;

    public ClientsProvider(@NotNull final String host, final int port, final int N, final int M, final long delta, final int X) {
        this.host = host;
        this.port = port;
        this.N = N;
        this.M = M;
        this.delta = delta;
        this.X = X;
    }

    public long run() {
        final List<Thread> threads = new ArrayList<>();
        final List<Long> times = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < M; i++) {
            threads.add(new Thread(createTask(times)));
        }

        threads.forEach(Thread::start);

        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException ignored) {
            }
        });

        return (long) times.stream().mapToDouble(i -> i).average().orElse(0);

    }

    protected abstract Runnable createTask(@NotNull final List<Long> times);
}
