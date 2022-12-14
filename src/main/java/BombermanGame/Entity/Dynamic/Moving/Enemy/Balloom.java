package BombermanGame.Entity.Dynamic.Moving.Enemy;

import BombermanGame.Entity.Dynamic.Moving.DIRECTION;
import BombermanGame.Entity.Dynamic.Moving.MOVING_ENTITY_ACTION;
import BombermanGame.Entity.Dynamic.NotMoving.Brick;
import BombermanGame.Entity.Entity;
import BombermanGame.Entity.Still.Wall;
import javafx.scene.image.Image;

public class Balloom extends Enemy {

    public static final DIRECTION DEFAULT_DIRECTION = DIRECTION.RIGHT;
    public static final MOVING_ENTITY_ACTION DEFAULT_ACTION = MOVING_ENTITY_ACTION.MOVING;
    public static final int DEFAULT_SPEED = 1;
    private static final int DEFAULT_BONUS_SCORE = 50;

    @Override
    protected void setDefaultSpecifications(Object... specifications) {
        direction = (DIRECTION) specifications[0];
        setAction((MOVING_ENTITY_ACTION) specifications[1]);
        speed = (int) specifications[2];
    }

    @Override
    protected void initBonusScore() {
        bonusScore = DEFAULT_BONUS_SCORE;
    }

    public Balloom(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        setDefaultSpecifications(DEFAULT_DIRECTION, DEFAULT_ACTION, DEFAULT_SPEED);
    }

    public Balloom(int x, int y) {
        super(x, y);
        setDefaultSpecifications(DEFAULT_DIRECTION, DEFAULT_ACTION, DEFAULT_SPEED);
    }

    @Override
    public void update() {
        super.update();
    }
}
