package BombermanGame.Entity.Dynamic.NotMoving;

import BombermanGame.Entity.Dynamic.DynamicEntity;
import javafx.scene.image.Image;

public abstract class NotMovingEntity extends DynamicEntity {

    public NotMovingEntity(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }
}
