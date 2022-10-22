package BombermanGame.Entity.Still;

import BombermanGame.Entity.Entity;
import javafx.scene.image.Image;

public class Wall extends StillEntity {

    public Wall(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public Wall(int x, int y) {
        super(x, y);
    }

    @Override
    public void update() {

    }

    @Override
    public void collide(Entity entity) {
        
    }
}
