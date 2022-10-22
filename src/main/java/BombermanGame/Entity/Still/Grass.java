package BombermanGame.Entity.Still;

import BombermanGame.Entity.Entity;
import javafx.scene.image.Image;

public class Grass extends StillEntity {

    public Grass(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public Grass(int x, int y) {
        super(x, y);
    }

    @Override
    public void update() {

    }

    @Override
    public void collide(Entity entity) {

    }
}
