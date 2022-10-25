package BombermanGame.Entity.Dynamic.Moving;

import BombermanGame.Entity.Dynamic.DynamicEntity;
import BombermanGame.Entity.Entity;
import javafx.scene.image.Image;

public abstract class MovingEntity extends DynamicEntity {
    protected boolean isDead = false;
    protected DIRECTION direction = DIRECTION.DOWN;
    protected MOVING_ENTITY_ACTION action;
    protected int speed;

    public DIRECTION getDirection() {
        return direction;
    }

    public void setDirection(DIRECTION direction) {
        this.direction = direction;
    }

    public MOVING_ENTITY_ACTION getAction() {
        return action;
    }

    public void setAction(MOVING_ENTITY_ACTION action) {
        this.action = action;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    protected MovingEntity() {
        super();
    }
    public MovingEntity(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public MovingEntity(int x, int y) {
        super(x, y);
    }

    public abstract void update();

    public abstract void collide(Entity entity);
}
