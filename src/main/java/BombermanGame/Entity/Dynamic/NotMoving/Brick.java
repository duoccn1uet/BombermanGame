package BombermanGame.Entity.Dynamic.NotMoving;

import BombermanGame.Entity.Entity;
import javafx.scene.image.Image;

public class Brick extends NotMovingEntity {

    public Brick(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    protected void setDefaultSpecifications(Object... specifications) {

    }

    public Brick(int x, int y) {
        super(x, y);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void collide(Entity entity) {

    }
}
