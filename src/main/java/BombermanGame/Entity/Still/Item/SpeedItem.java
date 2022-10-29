package BombermanGame.Entity.Still.Item;

import BombermanGame.Entity.Dynamic.NotMoving.Brick;
import BombermanGame.Entity.Entity;
import javafx.scene.image.Image;

public class SpeedItem extends Item {
    public static long DEFAULT_APPLY_TIME = (long) 10e9;

    public SpeedItem(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public SpeedItem(int x, int y) {
        super(x, y);
    }

    @Override
    protected void initApplyTime() {
        applyDuration = DEFAULT_APPLY_TIME;
    }

    public SpeedItem(Brick brick) {
        super(brick);
    }

    @Override
    public void collide(Entity entity) {

    }

    @Override
    public void update() {

    }
}
