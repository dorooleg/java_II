package ru.spbau.mit.protocol;

import org.jetbrains.annotations.NotNull;
import ru.spbau.mit.ManagerProtos;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ManagerProtocol {

    @NotNull
    private final InputStream input;
    @NotNull
    private final OutputStream output;

    public ManagerProtocol(@NotNull final InputStream input, @NotNull final OutputStream output) {
        this.input = input;
        this.output = output;
    }

    public void sendStart(@NotNull final ManagerProtos.StartMessage.Type type) throws IOException {
        final ManagerProtos.StartMessage startMessage = ManagerProtos
                .StartMessage
                .newBuilder()
                .setType(type)
                .build();

        ManagerProtos
                .RequestMessage
                .newBuilder()
                .setStart(startMessage)
                .build()
                .writeDelimitedTo(output);
    }

    public void sendStop() throws IOException {
        final ManagerProtos.StopMessage stopMessage = ManagerProtos
                .StopMessage
                .newBuilder()
                .build();

        ManagerProtos
                .RequestMessage
                .newBuilder()
                .setStop(stopMessage)
                .build()
                .writeDelimitedTo(output);
    }

    public void sendDisconnect() throws IOException {
        final ManagerProtos.DisconnectMessage disconnectMessage = ManagerProtos
                .DisconnectMessage
                .newBuilder()
                .build();

        ManagerProtos
                .RequestMessage
                .newBuilder()
                .setDisconnect(disconnectMessage)
                .build()
                .writeDelimitedTo(output);
    }

    public void sendResponse(final double averageRequestTime, double averageResponseTime) throws IOException {
        final ManagerProtos.ResponseMessage responseMessage = ManagerProtos
                .ResponseMessage
                .newBuilder()
                .setAverageRequestTime(averageRequestTime)
                .setAverageResponseTime(averageResponseTime)
                .build();

        ManagerProtos
                .RequestMessage
                .newBuilder()
                .setResponse(responseMessage)
                .build()
                .writeDelimitedTo(output);
    }

    public ManagerProtos.RequestMessage receiveRequest() throws IOException {
        return ManagerProtos
                .RequestMessage
                .parseDelimitedFrom(input);
    }
}
