package BombermanGame.Entity;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position up(int speed) {
        return new Position(x, y - speed);
    }

    public Position down(int speed) {
        return new Position(x, y + speed);
    }

    public Position left(int speed) {
        return new Position(x - speed, y);
    }

    public Position right(int speed) {
        return new Position(x + speed, y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
