package BombermanGame.Entity.Dynamic.Moving;

import BombermanGame.KeyEventHandler.KeyEventListener;
import javafx.event.Event;
import javafx.event.EventType;
import BombermanGame.Entity.Dynamic.Moving.Enemy.Enemy;
import BombermanGame.Entity.Dynamic.NotMoving.Brick;
import BombermanGame.Entity.Entity;
import BombermanGame.Entity.Still.Wall;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Arrays;
import java.util.List;

import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCode.S;

public class Bomber extends MovingEntity implements KeyEventListener {
    private static final int[] changeX = {0, 1, 0, -1};
    private static final int[] changeY = {-1, 0, 1, 0};

    public static final DIRECTION DEFAULT_DIRECTION = DIRECTION.RIGHT;
    public static final MOVING_ENTITY_ACTION DEFAULT_ACTION = MOVING_ENTITY_ACTION.STOP;
    public static final int DEFAULT_SPEED = 3;
    /**
     * A: Move left;
     * D: Move right;
     * W: Move up;
     * S: Move down;
     * SPACE: Bomb;
     */
    private final List<KeyCode> keyCodes = Arrays.asList(A, D, W, S, SPACE);
    private KeyCode currentlyPressed;

    private Bomber() {
        super();
    }

    @Override
    protected void setDefaultSpecifications(Object... specifications) {
        direction = (DIRECTION) specifications[0];
        action = (MOVING_ENTITY_ACTION) specifications[1];
        speed = (int) specifications[2];
        animation.load(this);
    }

    public Bomber(int x, int y) {
        super(x, y);
        setDefaultSpecifications(DEFAULT_DIRECTION, DEFAULT_ACTION, DEFAULT_SPEED);
        animation.load(this);
    }

    public Bomber(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        setDefaultSpecifications(DEFAULT_DIRECTION, DEFAULT_ACTION, DEFAULT_SPEED);
    }

    @Override
    public void update() {
        img = animation.getCurrentImage();
    }

    @Override
    public List<KeyCode> interestedIn() {
        return keyCodes;
    }

    private DIRECTION getDirection(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case A:
                return DIRECTION.LEFT;
            case W:
                return DIRECTION.UP;
            case S:
                return DIRECTION.DOWN;
            case D:
                return DIRECTION.RIGHT;
            default:
                throw new IllegalArgumentException("Bomber has not direction key event" + keyEvent.getCode());
        }
    }

    private void changeXYByDirection() {
        setX(getX() + speed * changeX[direction.getValue()]);
        setY(getY() + speed * changeY[direction.getValue()]);
    }
    @Override
    public void notify(KeyEvent keyEvent) {
        EventType<? extends Event> eventType = keyEvent.getEventType();
        if (KeyEvent.KEY_RELEASED.equals(eventType)) {
            if (keyEvent.getCode().equals(currentlyPressed)) {
                action = MOVING_ENTITY_ACTION.STOP;
                this.speed = 0;
            }
        } else if (KeyEvent.KEY_PRESSED.equals(eventType)) {
            currentlyPressed = keyEvent.getCode();
            this.direction = getDirection(keyEvent);
            action = MOVING_ENTITY_ACTION.MOVING;
            this.speed = DEFAULT_SPEED;
            changeXYByDirection();
        }
    }

    @Override
    public void collide(Entity entity) {
        if(entity instanceof Wall)
            collide((Wall) entity);
        else if(entity instanceof Brick)
            collide((Brick) entity);
        else if(entity instanceof Enemy)
            collide((Enemy) entity);
    }

    protected void collide(Wall wall) {
        if(last != null) {
            position = last;
        }
        last = null;
    }

    protected void collide(Brick brick) {
        if(last != null) {
            position = last;
        }
        last = null;
    }

    protected void collide(Enemy enemy) {
        if(!isDead) {
            isDead = true;
            speed = 0;
        }
    }
}
