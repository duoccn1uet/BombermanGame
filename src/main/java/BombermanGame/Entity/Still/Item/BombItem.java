package BombermanGame.Entity.Still.Item;

import BombermanGame.Entity.Dynamic.NotMoving.Brick;
import BombermanGame.Entity.Entity;
import javafx.scene.image.Image;

public class BombItem extends Item {
    public static final long DEFAULT_APPLY_TIME = -1;
    private static final int DEFAULT_BONUS_SCORE = 20;

    public BombItem(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public BombItem(int x, int y) {
        super(x, y);
    }

    @Override
    protected void initApplyDuration() {
        applyDuration = DEFAULT_APPLY_TIME;
    }

    @Override
    protected void initBonusScore() {
        bonusScore = DEFAULT_BONUS_SCORE;
    }

    @Override
    public ITEM_TYPE getType() {
        return ITEM_TYPE.BOMB;
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
