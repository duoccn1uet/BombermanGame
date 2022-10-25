package BombermanGame.Entity.Dynamic.Moving.Enemy;

import BombermanGame.Entity.Direction;
import BombermanGame.Entity.Dynamic.Moving.DIRECTION;
import BombermanGame.Entity.Dynamic.Moving.MovingEntity;
import BombermanGame.Entity.Dynamic.NotMoving.Brick;
import BombermanGame.Entity.Entity;
import BombermanGame.Entity.Position;
import BombermanGame.Entity.Still.Wall;
import javafx.scene.image.Image;

import java.util.Random;

public class Enemy extends MovingEntity {

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

    public void changeDirection() {
        DIRECTION tmp;
        direction = (tmp = randomDirection()) == direction ? randomDirection() : tmp;
    }

    public DIRECTION randomDirection() {
        Random random = new Random();
        return DIRECTION.values()[random.nextInt(DIRECTION.values().length)];
    }

    public void moveToPos(Position newPosition) {
        position.setX(newPosition.getX());
        position.setY(newPosition.getY());
    }

    @Override
    public void collide(Entity entity) {
        if(entity instanceof Wall)
            collide((Wall) entity);
        if(entity instanceof Brick)
            collide((Brick) entity);
    }

    protected void collide(Wall wall) {
        if(last != null)
            position = last;
        last = null;
        moveToPos(position);
        changeDirection();
    }

    protected void collide(Brick brick) {
        if(last != null)
            position = last;
        last = null;
        moveToPos(position);
        changeDirection();
    }

}
