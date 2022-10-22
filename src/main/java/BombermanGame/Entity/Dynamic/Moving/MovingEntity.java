package BombermanGame.Entity.Dynamic.Moving;

import BombermanGame.Entity.Dynamic.DynamicEntity;
import BombermanGame.Entity.Entity;
import javafx.scene.image.Image;

public abstract class MovingEntity extends DynamicEntity {
    protected boolean isDead = false;
    public MovingEntity(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public MovingEntity(int x, int y) {
        super(x, y);
    }

    public abstract void update();

    public abstract void collide(Entity entity);
}
