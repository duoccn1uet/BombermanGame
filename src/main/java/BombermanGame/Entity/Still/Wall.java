package BombermanGame.Entity.Still;

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
}
