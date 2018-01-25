package ru.spbau.mit.servers.statistics;

public class Statistic {
    private long clientTime;
    private long requestTime;
    private long processTime;

    public Statistic(final long clientTime, final long requestTime, final long processTime) {
        this.clientTime = clientTime;
        this.requestTime = requestTime;
        this.processTime = processTime;
    }

    public Statistic(final long requestTime, final long processTime) {
        this.requestTime = requestTime;
        this.processTime = processTime;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public long getProcessTime() {
        return processTime;
    }

    public long getClientTime() {
        return clientTime;
    }

}
