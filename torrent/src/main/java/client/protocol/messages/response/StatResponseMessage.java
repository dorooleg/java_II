package client.protocol.messages.response;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class StatResponseMessage {

    @NotNull
    private final Set<Integer> blocks;

    public StatResponseMessage(@NotNull final Set<Integer> blocks) {
        this.blocks = blocks;
    }

    @NotNull
    public Set<Integer> getBlocks() {
        return blocks;
    }
}
