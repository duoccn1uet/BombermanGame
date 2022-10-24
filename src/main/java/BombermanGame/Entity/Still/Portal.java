package BombermanGame.Entity.Still;

import BombermanGame.Entity.Entity;
import javafx.scene.image.Image;

public class Portal extends StillEntity {

    public Portal(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public Portal(int x, int y) {
        super(x, y);
    }

    @Override
    public void update() {

    }

    @Override
    public void collide(Entity entity) {

    }

}
