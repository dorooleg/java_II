package client.protocol.messages;

public enum TypeMessage {
    STAT(1), GET(2);

    private final int id;

    TypeMessage(final int id) {
        this.id = id;
    }

    public static TypeMessage getEnum(final int id) {
        for (final TypeMessage e : TypeMessage.values()) {
            if (e.getId() == id) {
                return e;
            }
        }
        throw new IllegalArgumentException("id " + id + " incorrect");
    }

    public int getId() {
        return id;
    }
}
