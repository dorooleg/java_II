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
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.*;

public class AsyncServer implements IServer {
    @org.jetbrains.annotations.NotNull
    private final static Logger logger = Logger.getLogger(AsyncServer.class);

    @NotNull
    private final Set<AsynchronousSocketChannel> channels = Collections.synchronizedSet(new HashSet<>());
    @NotNull
    private final List<Statistic> statistics = Collections.synchronizedList(new ArrayList<>());
    private final int port;
    private AsynchronousServerSocketChannel serverChannel;

    public AsyncServer(final int port) {
        logger.debug("create");
        this.port = port;
    }

    private void removeAsyncChannel(AsynchronousSocketChannel channel) {
        try {
            channel.close();
        } catch (IOException ignored) {
        } finally {
            channels.remove(channel);
        }
    }

    private void handleError(Throwable e) {
        logger.debug("handleError");
    }

    private void readArray(AsynchronousSocketChannel channel) {

        ByteBuffer intReadBuf = ByteBuffer.allocate(Integer.BYTES);

        channel.read(intReadBuf, null, new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer result, Object attachment) {
                logger.debug("read");

                if (intReadBuf.hasRemaining()) {
                    channel.read(intReadBuf, null, this);
                    return;
                }

                intReadBuf.flip();
                int length = intReadBuf.getInt();

                if (length == ArrayProtos.ArrayMessage.Type.DISCONNECT.getNumber()) {
                    removeAsyncChannel(channel);
                    return;
                }

                final long startRequest = System.nanoTime();

                ByteBuffer buffer = ByteBuffer.wrap(new byte[length]);
                channel.read(buffer, null, new CompletionHandler<Integer, Object>() {
                    @Override
                    public void completed(Integer result, Object attachment) {
                        if (buffer.hasRemaining()) {
                            channel.read(intReadBuf, null, this);
                            return;
                        }
                        try {
                            writeArray(channel, startRequest, ArrayProtos.ArrayMessage.parseFrom(buffer.array()));
                        } catch (InvalidProtocolBufferException ignored) {
                        }
                    }

                    @Override
                    public void failed(Throwable exc, Object attachment) {
                        handleError(exc);
                    }
                });
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                handleError(exc);
            }
        });
    }

    private void writeArray(AsynchronousSocketChannel channel,
                            long startRequestTime,
                            ArrayProtos.ArrayMessage request) {

        long startProc = System.nanoTime();
        ArrayProtos.ArrayMessage answer = ArrayProtos.ArrayMessage.newBuilder().addAllValues(Sorts.bubbleSort(request.getValuesList())).build();
        long procTime = System.nanoTime() - startProc;

        byte[] bs = answer.toByteArray();
        byte[] data = new byte[bs.length + Integer.BYTES];

        System.arraycopy(ByteBuffer.allocate(Integer.BYTES).putInt(0, bs.length).array(), 0, data, 0, Integer.BYTES);
        System.arraycopy(bs, 0, data, Integer.BYTES, bs.length);

        channel.write(ByteBuffer.wrap(data), null, new CompletionHandler<Integer, Object>() {
                    @Override
                    public void completed(Integer result, Object attachment) {
                        logger.debug("write");
                        long endReqTime = System.nanoTime();
                        statistics.add(new Statistic(endReqTime - startRequestTime, procTime));
                        readArray(channel);
                    }

                    @Override
                    public void failed(Throwable exc, Object attachment) {
                        handleError(exc);
                    }
                }
        );
    }

    public void start() throws IOException {
        logger.debug("start");
        serverChannel = AsynchronousServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(port));
        serverChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(AsynchronousSocketChannel result, Object attachment) {
                logger.debug("accept");
                channels.add(result);

                readArray(result);

                serverChannel.accept(null, this);
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                handleError(exc);
            }
        });
    }

    public void stop() throws IOException {
        logger.debug("stop");
        serverChannel.close();
        for (AsynchronousSocketChannel chanel : channels) {
            chanel.close();
        }
    }

    @NotNull
    public List<Statistic> getStatistics() {
        return statistics;
    }

}
