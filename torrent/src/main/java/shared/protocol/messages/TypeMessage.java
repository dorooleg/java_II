package shared.protocol.messages;

public enum TypeMessage {
    LIST(1), UPLOAD(2), SOURCES(3), UPDATE(4);

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
