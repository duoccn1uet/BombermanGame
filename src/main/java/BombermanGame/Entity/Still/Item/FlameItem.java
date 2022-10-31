package BombermanGame.Entity.Still.Item;

import BombermanGame.Entity.Dynamic.NotMoving.Brick;
import BombermanGame.Entity.Entity;
import javafx.scene.image.Image;

public class FlameItem extends Item {
    private static final int DEFAULT_BONUS_SCORE = 20;
    public static long DEFAULT_APPLY_TIME = (long) 30e9;

    public FlameItem(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public FlameItem(int x, int y) {
        super(x, y);
    }

    @Override
    protected void initApplyDuration() {
        applyDuration = DEFAULT_APPLY_TIME;
        remainingTime = (int) DEFAULT_APPLY_TIME / (int) 1e9;
    }

    @Override
    protected void initBonusScore() {
        bonusScore = DEFAULT_BONUS_SCORE;
    }

    @Override
    public ITEM_TYPE getType() {
        return ITEM_TYPE.FLAME;
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
