package BombermanGame.Entity.Still;

import BombermanGame.Entity.Entity;
import javafx.scene.image.Image;

public abstract class StillEntity extends Entity {

    public StillEntity() {}
    public StillEntity(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public StillEntity(int x, int y) {
        super(x, y);
    }

    public abstract void update();
}
