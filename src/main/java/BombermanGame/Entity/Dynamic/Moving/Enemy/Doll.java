package BombermanGame.Entity.Dynamic.Moving.Enemy;

import BombermanGame.BombermanGame;
import BombermanGame.Entity.Dynamic.Moving.DIRECTION;
import BombermanGame.Entity.Dynamic.Moving.MOVING_ENTITY_ACTION;
import BombermanGame.Entity.Dynamic.NotMoving.Bomb;
import BombermanGame.Entity.Entity;
import BombermanGame.Entity.Position;
import BombermanGame.Entity.Still.StillEntity;
import BombermanGame.Sprite.Sprite;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Doll extends Enemy{

    public static final DIRECTION DEFAULT_DIRECTION = DIRECTION.RIGHT;
    public static final MOVING_ENTITY_ACTION DEFAULT_ACTION = MOVING_ENTITY_ACTION.MOVING;
    public static final int DEFAULT_SPEED = 1;
    private static final int DEFAULT_BONUS_SCORE = 50;
    private static final long TELEPORT_COOLDOWN = (long) 1000000000 * 5;
    private static final long TELEPORT_RADIUS = 4;
    private long LAST_TELEPORT = 0;

    public Doll(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        setDefaultSpecifications(DEFAULT_DIRECTION, DEFAULT_ACTION, DEFAULT_SPEED);
    }

    public Doll(int x, int y) {
        super(x, y);
        setDefaultSpecifications(DEFAULT_DIRECTION, DEFAULT_ACTION, DEFAULT_SPEED);
    }

    @Override
    public Position move() {
        Position newPosition;

        long time = BombermanGame.getTime();
        if(time - this.LAST_TELEPORT > TELEPORT_COOLDOWN) {
            this.LAST_TELEPORT = time;
            newPosition = teleport();
        } else {
            newPosition = super.move();
        }

        return newPosition;
    }

    private Position teleport() {
        Random random = new Random();
        while(true) {
            int i = random.nextInt(9) - 4;
            int j = random.nextInt(9) - 4;

            if(Math.abs(i) + Math.abs(j) <= TELEPORT_RADIUS && inBoard(this.getBoardX() + i, this.getBoardY() + j)) {
                return new Position((this.getBoardX() + i) * Sprite.SCALED_SIZE,
                                        (this.getBoardY() + j) * Sprite.SCALED_SIZE);
            }
        }
    }

    private boolean inBoard(int x, int y) {
        return x > 0 && x < BombermanGame.WIDTH && y > 0 && y < BombermanGame.HEIGHT;
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

    @Override
    public void update() {
        if (this.getAction() == MOVING_ENTITY_ACTION.MOVING) {
            last = new Position(position.getX(), position.getY());
            position = move();
        }
        super.update();
    }

}
