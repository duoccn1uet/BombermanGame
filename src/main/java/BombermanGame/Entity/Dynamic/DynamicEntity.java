package BombermanGame.Entity.Dynamic;

import BombermanGame.Entity.Dynamic.Moving.MOVING_ENTITY_STATUS;
import BombermanGame.Entity.Entity;
import javafx.scene.image.Image;

public abstract class DynamicEntity extends Entity {
    protected Animation animation = new Animation();
    public DynamicEntity(int x, int y) {
        super(x, y);
        animation.load(this);
    }

    public DynamicEntity(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        animation.load(this);
    }
}
