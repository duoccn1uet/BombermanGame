package BombermanGame.Entity.Dynamic;

import BombermanGame.Entity.Direction;
import BombermanGame.Entity.Entity;
import BombermanGame.Entity.Position;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;

public abstract class DynamicEntity extends Entity {
    protected Animation animation = new Animation();

    protected Position last;
    protected Direction direction;
    protected int speed;

    public DynamicEntity(int x, int y) {
        super(x, y);
        last = new Position(x, y);
        direction = Direction.R;
        animation.load(this);
    }

    public DynamicEntity(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        last = new Position(xUnit, yUnit);
        direction = Direction.R;
        animation.load(this);
    }

    public abstract void update();
}
