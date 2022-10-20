package BombermanGame.Entity.Dynamic.NotMoving;

import javafx.scene.image.Image;

public class Brick extends NotMovingEntity {

    public Brick(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public Brick(int x, int y) {
        super(x, y);
    }

    @Override
    public void update() {

    }
}
