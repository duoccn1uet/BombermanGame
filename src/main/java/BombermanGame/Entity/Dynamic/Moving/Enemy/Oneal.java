package BombermanGame.Entity.Dynamic.Moving.Enemy;

import BombermanGame.Entity.Dynamic.Moving.DIRECTION;
import BombermanGame.Entity.Dynamic.Moving.MOVING_ENTITY_ACTION;
import BombermanGame.Entity.Dynamic.NotMoving.Brick;
import BombermanGame.Entity.Entity;
import BombermanGame.Entity.Still.Wall;
import javafx.scene.image.Image;

public class Oneal extends Enemy {

    public static final DIRECTION DEFAULT_DIRECTION = DIRECTION.RIGHT;
    public static final MOVING_ENTITY_ACTION DEFAULT_ACTION = MOVING_ENTITY_ACTION.STOP;
    public static final int DEFAULT_SPEED = 1;

    public Oneal(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        setDefaultSpecifications(DEFAULT_DIRECTION, DEFAULT_ACTION, DEFAULT_SPEED);
    }


    public Oneal(int x, int y) {
        super(x, y);
        setDefaultSpecifications(DEFAULT_DIRECTION, DEFAULT_ACTION, DEFAULT_SPEED);
    }

    @Override
    protected void setDefaultSpecifications(Object... specifications) {
        direction = (DIRECTION) specifications[0];
        action = (MOVING_ENTITY_ACTION) specifications[1];
        speed = (int) specifications[2];
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

    @Override
    public void update() {
        super.update();
    }
}
