package BombermanGame.Entity.Dynamic.NotMoving;

import BombermanGame.Entity.Entity;
import javafx.scene.image.Image;

public class Explosion extends NotMovingEntity {
    public Explosion(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public Explosion(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean isVanished() {
        return animation.getAnimationLoop() >= 1;
    }

    @Override
    protected void setDefaultSpecifications(Object... specifications) {

    }

    @Override
    public void collide(Entity entity) {

    }
}
