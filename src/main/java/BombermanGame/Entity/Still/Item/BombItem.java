package BombermanGame.Entity.Still.Item;

import BombermanGame.Entity.Dynamic.NotMoving.Brick;
import BombermanGame.Entity.Entity;
import javafx.scene.image.Image;

public class BombItem extends Item {
    public static final long DEFAULT_APPLY_TIME = -1;
    public BombItem(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public BombItem(int x, int y) {
        super(x, y);
    }

    @Override
    protected void initApplyTime() {
        applyDuration = DEFAULT_APPLY_TIME;
    }

    public BombItem(Brick brick) {
        super(brick);
    }

    @Override
    public void collide(Entity entity) {

    }

    @Override
    public void update() {

    }
}
