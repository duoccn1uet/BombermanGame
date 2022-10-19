package BombermanGame.Entity.Dynamic;

import BombermanGame.Entity.Entity;
import javafx.scene.image.Image;

public abstract class DynamicEntity extends Entity {
    public DynamicEntity() {
        super();
    }
    public DynamicEntity(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }
}
