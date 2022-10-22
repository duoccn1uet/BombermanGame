package BombermanGame.Entity.Dynamic.Moving;

import BombermanGame.KeyEventHandler.KeyEventListener;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Arrays;
import java.util.List;

import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCode.S;

public class Bomber extends MovingEntity implements KeyEventListener {
    /**
     *      A: Move left;
     *      D: Move right;
     *      W: Move up;
     *      S: Move down;
     *      SPACE: Bomb;
     */
    private final List<KeyCode> keyCodes = Arrays.asList(A, D, W, S, SPACE);
    private KeyCode currentlyPressed;

    @Override
    void setSpeed(int speed) {
        this.speed = speed;
    }

    public Bomber(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        setSpeed(1);
    }

    public Bomber(int x, int y) {
        super(x, y);
    }

    @Override
    public void update() {
        img = animation.getCurrentImage();
    }

    @Override
    public List<KeyCode> interestedIn() {
        return keyCodes;
    }

    public DIRECTION getDirection() {
        return direction;
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
    @Override
    public void notify(KeyEvent keyEvent) {
        EventType<? extends Event> eventType = keyEvent.getEventType();
        if(KeyEvent.KEY_RELEASED.equals(eventType)) {
            if(keyEvent.getCode().equals(currentlyPressed)) {
                this.speed = 0;
            }
        } else if(KeyEvent.KEY_PRESSED.equals(keyEvent)) {
            currentlyPressed = keyEvent.getCode();
            this.direction = getDirection(keyEvent);
            this.speed = 1;
        }
    }
}
