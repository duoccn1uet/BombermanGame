package BombermanGame.Entity.Dynamic.Moving.Enemy;

import BombermanGame.Entity.Dynamic.Moving.DIRECTION;
import BombermanGame.Entity.Dynamic.Moving.MOVING_ENTITY_ACTION;
import javafx.scene.image.Image;

public class Doll extends Enemy{

    public static final DIRECTION DEFAULT_DIRECTION = DIRECTION.RIGHT;
    public static final MOVING_ENTITY_ACTION DEFAULT_ACTION = MOVING_ENTITY_ACTION.MOVING;
    public static final int DEFAULT_SPEED = 1;
    private static final int DEFAULT_BONUS_SCORE = 50;

    public Doll(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        setDefaultSpecifications(DEFAULT_DIRECTION, DEFAULT_ACTION, DEFAULT_SPEED);
    }

    public Doll(int x, int y) {
        super(x, y);
        setDefaultSpecifications(DEFAULT_DIRECTION, DEFAULT_ACTION, DEFAULT_SPEED);
    }

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
}
