package ru.spbau.mit.servers.tcp;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.ArrayProtos;
import ru.spbau.mit.algorithms.Sorts;
import ru.spbau.mit.servers.IServer;
import ru.spbau.mit.servers.statistics.Statistic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NonBlockingServer implements IServer {

    @org.jetbrains.annotations.NotNull
    private final static Logger logger = Logger.getLogger(CachedThreadPoolServer.class);

    private final int port;
    @NotNull
    private final ExecutorService pool;
    @NotNull
    private final List<Statistic> statistics = Collections.synchronizedList(new ArrayList<>());
    @NotNull
    private final Set<SocketChannel> channels = new HashSet<>();
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private Thread thread;

    public NonBlockingServer(int port, int poolSize) {
        logger.debug("create");
        this.port = port;
        pool = Executors.newFixedThreadPool(poolSize);
    }

    public void start() throws IOException {
        logger.debug("start");
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        thread = new Thread(new SelectorResolver());
        thread.start();
    }

    public void stop() throws IOException {
        logger.debug("stop");
        thread.interrupt();
        selector.close();
        serverSocketChannel.close();
        for (SocketChannel channel : channels) {
            channel.close();
        }
    }

    @NotNull
    public List<Statistic> getStatistics() {
        return statistics;
    }

    enum State {
        READY,
        READ_HEADER,
        READ_BODY,
        CALCULATION,
        CALCULATION_DONE,
        WRITE,
        FINISH,
        DISCONNECT
    }

    private class SelectorResolver implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    resolve();
                } catch (Exception e) {
                    if (Thread.currentThread().isInterrupted()) {
                        logger.debug("interrupt");
                        return;
                    }
                }
            }
        }

        private void resolve() throws IOException {
            selector.select();
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();

                if (!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) {
                    accept(key);
                }
                if (key.isReadable()) {
                    read(key);
                }
                if (key.isWritable()) {
                    write(key);
                }
            }
        }

        private void accept(SelectionKey key) throws IOException {
            logger.debug("accept");
            SocketChannel clientSocket = ((ServerSocketChannel) key.channel()).accept();
            clientSocket.configureBlocking(false);
            clientSocket.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, new NonBlockingTask(clientSocket));
            channels.add(clientSocket);
        }

        private void read(SelectionKey key) throws IOException {
            logger.debug("read");
            NonBlockingTask task = (NonBlockingTask) key.attachment();
            task.read(key);
            if (task.isDisconnected()) {
                key.cancel();
                SocketChannel clientSocket = task.getClientSocket();
                channels.remove(clientSocket);
                clientSocket.close();
            }
        }

        private void write(SelectionKey key) throws IOException {
            logger.debug("write");
            NonBlockingTask task = (NonBlockingTask) key.attachment();
            Optional<Statistic> result = task.write(key);
            if (result.isPresent()) {
                Statistic stats = result.get();
                statistics.add(stats);
                task.reset();
            }
        }

        private class NonBlockingTask {
            private SocketChannel clientSocket;
            private State state = State.READY;

            private ByteBuffer[] request;
            private ByteBuffer[] response;

            private long requestTime = -1;
            private long processTime = -1;
            private Future result;

            NonBlockingTask(SocketChannel clientSocket) {
                this.clientSocket = clientSocket;
            }

            SocketChannel getClientSocket() {
                return clientSocket;
            }

            boolean isDisconnected() {
                return state == State.DISCONNECT;
            }

            void read(SelectionKey key) throws IOException {
                if (state == State.READY) {
                    logger.debug("READY");
                    requestTime = System.nanoTime();
                    state = State.READ_HEADER;
                    request = new ByteBuffer[2];
                    request[0] = ByteBuffer.allocate(Integer.BYTES);
                    request[0].clear();
                    return;
                }

                if (state == State.READ_HEADER) {
                    logger.debug("READ_HEADER");
                    SocketChannel channel = (SocketChannel) key.channel();
                    int read = channel.read(request[0]);

                    if (read < 0) {
                        channel.close();
                        key.cancel();
                        return;
                    }

                    if (request[0].hasRemaining()) {
                        return;
                    }

                    request[0].flip();

                    int size = request[0].getInt();
                    if (size == ArrayProtos.ArrayMessage.Type.DISCONNECT.getNumber()) {
                        logger.debug("DISCONNECT");
                        state = State.DISCONNECT;
                        return;
                    }

                    request[1] = ByteBuffer.allocate(size);
                    state = State.READ_BODY;
                } else if (state == State.READ_BODY) {
                    logger.debug("READ_BODY");
                    SocketChannel channel = (SocketChannel) key.channel();
                    int read = channel.read(request[1]);
                    if (read < 0) {
                        channel.close();
                        key.cancel();
                        return;
                    }
                    if (request[1].hasRemaining()) {
                        return;
                    }

                    request[1].flip();

                    state = State.CALCULATION;
                    logger.debug("CALCULATION");
                    result = pool.submit(() -> {
                        processTime = System.nanoTime();

                        request[0].flip();
                        int size = request[0].getInt();
                        byte[] buffer = new byte[size];
                        request[1].get(buffer);

                        ArrayProtos.ArrayMessage array;
                        try {
                            array = ArrayProtos.ArrayMessage.parseFrom(buffer);
                        } catch (InvalidProtocolBufferException ignored) {
                            return;
                        }

                        List<Integer> results = Sorts.bubbleSort(array.getValuesList());

                        final byte[] bytes = ArrayProtos.ArrayMessage.newBuilder().addAllValues(results).build().toByteArray();
                        ByteBuffer header = ByteBuffer.allocate(Integer.BYTES);
                        header.putInt(bytes.length);
                        header.flip();

                        ByteBuffer body = ByteBuffer.wrap(bytes);
                        response = new ByteBuffer[]{header, body};

                        processTime = System.nanoTime() - processTime;
                        state = State.CALCULATION_DONE;
                        logger.debug("CALCULATION_DONE");
                    });
                }
            }

            Optional<Statistic> write(SelectionKey key) throws IOException {
                SocketChannel channel = (SocketChannel) key.channel();

                if (result != null && result.isDone()) {
                    try {
                        result.get();
                        if (state == State.CALCULATION_DONE) {
                            state = State.WRITE;
                            logger.debug("WRITE");
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        key.cancel();
                        channel.close();
                        return Optional.empty();
                    }
                }

                if (state != State.WRITE) {
                    return Optional.empty();
                }

                if (response[0].hasRemaining() || response[1].hasRemaining()) {
                    channel.write(response);
                }

                if (!response[0].hasRemaining() && !response[1].hasRemaining()) {
                    state = State.FINISH;
                    logger.debug("FINISH");
                    return Optional.of(new Statistic(
                            System.nanoTime() - requestTime,
                            processTime));
                }

                return Optional.empty();
            }

            void reset() {
                logger.debug("reset");
                state = (state == State.DISCONNECT) ? State.DISCONNECT : State.READY;
                request = null;
                response = null;
                result = null;
            }
        }
    }
}
