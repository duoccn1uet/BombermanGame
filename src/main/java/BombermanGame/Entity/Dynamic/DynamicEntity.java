package BombermanGame.Entity.Dynamic;

import BombermanGame.Entity.Dynamic.Moving.DIRECTION;
import BombermanGame.Entity.Dynamic.Moving.MOVING_ENTITY_ACTION;
import BombermanGame.Entity.Entity;
import BombermanGame.Entity.Position;
import javafx.scene.image.Image;

public abstract class DynamicEntity extends Entity {
    protected Animation animation = new Animation();

    protected Position last;

    public DynamicEntity(int x, int y) {
        super(x, y);
    }

    public DynamicEntity(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    protected DynamicEntity() {
        super();
    }

    protected abstract void setDefaultSpecifications(Object... specifications);

    public abstract void update();
}
