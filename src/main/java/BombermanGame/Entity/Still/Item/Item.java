package BombermanGame.Entity.Still.Item;

import BombermanGame.CommonFunction;
import BombermanGame.Entity.Dynamic.NotMoving.Brick;
import BombermanGame.Entity.Still.StillEntity;
import BombermanGame.Sprite.Sprite;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Item extends StillEntity {
    protected Brick brick = null;
    protected Image insideBrick = null;
    protected long applyDuration;
    public static final int NUMBER_OF_ITEMS_TYPE = 3;

    /**
     * Duration can apply item (in nanosecond),
     * -1 if it is infinity
     */
    protected abstract void initApplyTime();

    public Item(Brick brick) {
        super(brick.getBoardX(), brick.getBoardY());
        this.brick = brick;
        insideBrick = Sprite.getFxImage(this.getEntityName(true) + "_inside_brick.png");
        initApplyTime();
    }

    public Item(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        initApplyTime();
    }

    public Item(int x, int y) {
        super(x, y);
        initApplyTime();
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
        int j = brick.getBoardX();
        int i = brick.getBoardY();
        int type = (int) CommonFunction.rand(1, NUMBER_OF_ITEMS_TYPE);
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
