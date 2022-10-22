package BombermanGame.Entity.Dynamic.Moving;

import BombermanGame.Entity.Dynamic.DynamicEntity;
import javafx.scene.image.Image;

public abstract class MovingEntity extends DynamicEntity {
    protected DIRECTION direction = DIRECTION.DOWN;
    protected MOVING_ENTITY_STATUS status = MOVING_ENTITY_STATUS.DEAD;

    public MOVING_ENTITY_STATUS getStatus() {
        return status;
    }

    public void setStatus(MOVING_ENTITY_STATUS status) {
        this.status = status;
    }
    protected int speed;

    abstract void setSpeed(int speed);
    public DIRECTION getDirection() {
        return direction;
    }
    public int getSpeed() {
        return speed;
    }
    public MovingEntity(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public MovingEntity(int x, int y) {
        super(x, y);
    }
}
