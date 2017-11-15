package mit.spbau.ru;

public enum Header {
    LIST(1),
    GET(2);

    private int type;

    Header(int type) {
        this.type = type;
    }

    public int type() {
        return type;
    }
}
