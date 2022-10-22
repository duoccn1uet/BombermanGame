package BombermanGame.Entity.Dynamic.Moving.Enemy;

import BombermanGame.Entity.Direction;
import BombermanGame.Entity.Dynamic.NotMoving.Brick;
import BombermanGame.Entity.Entity;
import BombermanGame.Entity.Still.Wall;
import javafx.scene.image.Image;

import java.util.Random;

public class Balloom extends Enemy {

    public Balloom(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        speed = 1;
    }

    public Balloom(int x, int y) {
        super(x, y);
        speed = 1;
    }

    public void changeDirection() {
        Direction tmp;
        direction = (tmp = randomDirection()) == direction ? randomDirection() : tmp;
    }

    public Direction randomDirection() {
        Random random = new Random();
        return Direction.values()[random.nextInt(Direction.values().length)];
    }

    @Override
    public void update() {
        super.update();
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
        changeDirection();
    }

    protected void collide(Brick brick) {
        if(last != null)
            position = last;
        last = null;
        changeDirection();
    }
}
