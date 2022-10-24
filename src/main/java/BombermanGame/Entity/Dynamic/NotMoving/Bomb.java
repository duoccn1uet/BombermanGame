package BombermanGame.Entity.Dynamic.NotMoving;

import BombermanGame.Entity.Entity;
import javafx.scene.image.Image;

public class Bomb extends NotMovingEntity {

    public Bomb(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }


    public Bomb(int x, int y) {
        super(x, y);
    }

    @Override
    protected void setDefaultSpecifications(Object... specifications) {

    }
    @Override
    public void collide(Entity entity) {

    }

}
