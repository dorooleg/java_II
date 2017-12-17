package shared.protocol.messages.response;

import org.jetbrains.annotations.NotNull;
import shared.protocol.messages.ClientInfo;

import java.util.List;

public class SourcesResponseMessage {
    @NotNull
    private final List<ClientInfo> clients;

    public SourcesResponseMessage(@NotNull List<ClientInfo> clients) {
        this.clients = clients;
    }

    @NotNull
    public List<ClientInfo> getClients() {
        return clients;
    }
}
