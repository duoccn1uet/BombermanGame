package BombermanGame.Entity.Dynamic.Moving.Enemy;

import BombermanGame.Entity.Dynamic.Moving.MovingEntity;
import BombermanGame.Entity.Position;
import javafx.scene.image.Image;

public abstract class Enemy extends MovingEntity {

    public Enemy(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public Enemy(int x, int y) {
        super(x, y);
    }

    public Position move() {
        Position newPosition;
        switch (direction) {
            case UP:
                newPosition = position.up(speed);
                break;
            case DOWN:
                newPosition = position.down(speed);
                break;
            case LEFT:
                newPosition = position.left(speed);
                break;
            default:
                newPosition = position.right(speed);
                break;
        }
        return newPosition;
    }

    @Override
    public void update() {
        if(isDead)
            return;
        last = new Position(position.getX(), position.getY());
        position = move();
    }
}
