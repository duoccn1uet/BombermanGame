package BombermanGame.Entity.Still.Item;

import BombermanGame.Entity.Dynamic.NotMoving.Brick;
import BombermanGame.Entity.Entity;
import javafx.scene.image.Image;

public class FlameItem extends Item {
    public static long DEFAULT_APPLY_TIME = (long) 10e9;

    public FlameItem(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public FlameItem(int x, int y) {
        super(x, y);
    }

    @Override
    protected void initApplyTime() {
        applyDuration = DEFAULT_APPLY_TIME;
    }

    public FlameItem(Brick brick) {
        super(brick);
    }

    @Override
    public void collide(Entity entity) {

    }

    @Override
    public void update() {

    }
}
