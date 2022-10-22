package BombermanGame.Entity.Dynamic.Moving;

public enum DIRECTION {
    UP(0),
    RIGHT(1),
    DOWN(2),
    LEFT(3),
    NONE(4);

    private final int value;
    public static final int size;
    static {
        size = values().length;
    }
    private DIRECTION(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public int size() {
        return size;
    }
}
