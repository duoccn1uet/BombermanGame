package BombermanGame.Entity.Dynamic.Moving;

import BombermanGame.Entity.Dynamic.DynamicEntity;
import javafx.scene.image.Image;

public abstract class MovingEntity extends DynamicEntity {
    public MovingEntity(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public MovingEntity(int x, int y) {
        super(x, y);
    }
}
