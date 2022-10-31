package BombermanGame.Entity.Still.Item;

import BombermanGame.CommonFunction;
import BombermanGame.CommonVar;
import BombermanGame.Entity.Dynamic.NotMoving.Brick;
import BombermanGame.Entity.Still.StillEntity;
import BombermanGame.Sprite.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.TimerTask;

public abstract class Item extends StillEntity {
    protected Brick brick = null;
    protected Image insideBrick = null;

    public int getBonusScore() {
        return bonusScore;
    }

    public static enum ITEM_TYPE {
        BOMB(0),
        FLAME(1),
        SPEED(2);

        private int value;
        ITEM_TYPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static int size() {
            return values().length;
        }
    }
    /**
     * period: nanosecond
     */
    protected long applyDuration;
    protected int remainingTime;
    protected int bonusScore;

    /**
     * Duration can apply item (in nanosecond),
     * -1 if it is infinity
     */
    protected abstract void initApplyDuration();
    protected abstract void initBonusScore();

    /**
     *
     * @return remaining time by second
     */
    public int getRemainingTime() {
        return remainingTime;
    }

    public abstract ITEM_TYPE getType();
    public Item(Brick brick) {
        super(brick.getBoardX(), brick.getBoardY());
        this.brick = brick;
        insideBrick = Sprite.getFxImage(this.getEntityName(true) + "_inside_brick.png");
        initApplyDuration();
        initBonusScore();
    }

    public Item(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        initApplyDuration();
    }

    public Item(int x, int y) {
        super(x, y);
        initApplyDuration();
    }

    @Override
    public void render(GraphicsContext gc) {
        if (brick == null || brick.isVanished()) {
            super.render(gc);
            brick = null;
        }
        else
            gc.drawImage(insideBrick, getX(), getY());
    }

    public boolean isInsideBrick() {
        return brick != null;
    }

    public long getApplyDuration() {
        return applyDuration;
    }

    public static Item randomItem(Brick brick) {
        int type = (int) CommonFunction.rand(1, ITEM_TYPE.size());
        switch (type) {
            case 1:
                return new BombItem(brick);
            case 2:
                return new FlameItem(brick);
            default:
                return new SpeedItem(brick);
        }
    }
}
