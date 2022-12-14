package BombermanGame.Entity.Dynamic.Moving.Enemy;

import BombermanGame.Entity.Dynamic.Moving.MOVING_ENTITY_ACTION;
import BombermanGame.Entity.Dynamic.Moving.DIRECTION;
import BombermanGame.Entity.Dynamic.Moving.MovingEntity;
import BombermanGame.Entity.Dynamic.NotMoving.Bomb;
import BombermanGame.Entity.Dynamic.NotMoving.Brick;
import BombermanGame.Entity.Entity;
import BombermanGame.Entity.Position;
import BombermanGame.Entity.Still.Wall;
import javafx.scene.image.Image;

import java.util.Random;

public abstract class Enemy extends MovingEntity {
    protected int bonusScore;

    protected abstract void initBonusScore();
    public int getBonusScore() {
        return bonusScore;
    }

    public Enemy(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        initBonusScore();
    }

    public Enemy(int x, int y) {
        super(x, y);
        initBonusScore();
    }

    public Position move() {
        Position newPosition;
        if(position.getX() % 64 == 32 && position.getY() % 64 == 32) {
            Random random = new Random();
            int rnd = random.nextInt(100);
            if(rnd < 20)
                changeDirection();
        }
        newPosition = super.move();
        return newPosition;
    }

    @Override
    public void update() {
        if (this.getAction() == MOVING_ENTITY_ACTION.MOVING && !(this instanceof Doll)) {
            last = new Position(position.getX(), position.getY());
            position = move();
        }
        super.update();
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
    public boolean isVanished() {
        return getAction() == MOVING_ENTITY_ACTION.DEAD && animation.finishCurrentAnimation();
    }

    @Override
    public void collide(Entity entity) {
        if(entity instanceof Wall)
            collide((Wall) entity);
        if(entity instanceof Brick)
            collide((Brick) entity);
        if(entity instanceof Bomb)
            collide((Bomb) entity);
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

    protected void collide(Bomb bomb) {
        if(last != null)
            position = last;
        last = null;
        moveToPos(position);
        changeDirection();
    }

}
