package BombermanGame.Entity.Dynamic.Moving;

import javafx.scene.image.Image;

public class Bomber extends MovingEntity {
    public Bomber(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public Bomber(int x, int y) {
        super(x, y);
    }

    @Override
    public void update() {

    }
}
