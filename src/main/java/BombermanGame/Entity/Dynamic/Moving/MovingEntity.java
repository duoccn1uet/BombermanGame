package BombermanGame.Entity.Dynamic.Moving;

import BombermanGame.Entity.Dynamic.DynamicEntity;
import BombermanGame.Entity.Entity;
import BombermanGame.Entity.Position;
import BombermanGame.Sprite.Sprite;
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

    public Position move() {
        Position newPosition;
        switch (direction) {
            case UP:
                if(position.getX() % Sprite.SCALED_SIZE == 0)
                    newPosition = position.up(speed);
                else if(position.getX() % Sprite.SCALED_SIZE < 16)
                    newPosition = position.left(1);
                else
                    newPosition = position.right(1);
                break;
            case DOWN:
                if(position.getX() % Sprite.SCALED_SIZE == 0)
                    newPosition = position.down(speed);
                else if(position.getX() % Sprite.SCALED_SIZE < 16)
                    newPosition = position.left(1);
                else
                    newPosition = position.right(1);
                break;
            case LEFT:
                if(position.getY() % Sprite.SCALED_SIZE == 0)
                    newPosition = position.left(speed);
                else if(position.getY() % Sprite.SCALED_SIZE < 16)
                    newPosition = position.up(1);
                else
                    newPosition = position.down(1);
                break;
            case RIGHT:
                if(position.getY() % Sprite.SCALED_SIZE == 0)
                    newPosition = position.right(speed);
                else if(position.getY() % Sprite.SCALED_SIZE < 16)
                    newPosition = position.up(1);
                else
                    newPosition = position.down(1);
                break;
            default:
                newPosition = position;
                break;
        }
        return newPosition;
    }

    public abstract void collide(Entity entity);
}
