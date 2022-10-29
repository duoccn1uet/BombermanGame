package BombermanGame.Entity.Dynamic.Moving;


import BombermanGame.CommonFunction;
import BombermanGame.CommonVar;
import BombermanGame.BombermanGame;
import BombermanGame.Entity.Dynamic.Moving.Enemy.Enemy;
import BombermanGame.Entity.Dynamic.NotMoving.Bomb;
import BombermanGame.Entity.Dynamic.NotMoving.Brick;
import BombermanGame.Entity.Entity;
import BombermanGame.Entity.Position;
import BombermanGame.Entity.Still.Item.BombItem;
import BombermanGame.Entity.Still.Item.FlameItem;
import BombermanGame.Entity.Still.Item.Item;
import BombermanGame.Entity.Still.Item.SpeedItem;
import BombermanGame.Entity.Still.Wall;
import BombermanGame.GAME_STATUS;
import BombermanGame.KeyEventHandler.KeyEventListener;
import BombermanGame.Sprite.Sprite;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Function;

import static javafx.scene.input.KeyCode.*;

public class Bomber extends MovingEntity implements KeyEventListener {
    public static final DIRECTION DEFAULT_DIRECTION = DIRECTION.RIGHT;
    public static final MOVING_ENTITY_ACTION DEFAULT_ACTION = MOVING_ENTITY_ACTION.STOP;
    public static final int DEFAULT_SPEED = 2;
    private final List<KeyCode> keyCodes = Arrays.asList(A, D, W, S, SPACE, ESCAPE);
    private KeyCode currentlyPressed;
    private int maxSpawnedBomb = 1;

    @Override
    protected void setDefaultSpecifications(Object... specifications) {
        direction = (DIRECTION) specifications[0];
        action = (MOVING_ENTITY_ACTION) specifications[1];
        speed = (int) specifications[2];
    }

    @Override
    public boolean isVanished() {
        return getAction() == MOVING_ENTITY_ACTION.DEAD && animation.finishCurrentAnimation();
    }

    public Bomber(int x, int y) {
        super(x, y);
        setDefaultSpecifications(DEFAULT_DIRECTION, DEFAULT_ACTION, DEFAULT_SPEED);
    }

    public Bomber(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        setDefaultSpecifications(DEFAULT_DIRECTION, DEFAULT_ACTION, DEFAULT_SPEED);
    }

    @Override
    public void update() {
        super.update();
        if(!isDead && action == MOVING_ENTITY_ACTION.MOVING) {
            this.last = new Position(position.getX(), position.getY());
            position = move(position, direction);
        }
        for (Item item : BombermanGame.itemList)
            if (!item.isInsideBrick() && isColliding(item)) {
                applyItem(item);
                BombermanGame.itemList.remove(item);
                break;
            }
    }

    private void applyItem(Item item) {
        if (item instanceof BombItem) {
            ++maxSpawnedBomb;
        } else if (item instanceof FlameItem) {
            Bomb.powerUp();
            CommonVar.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Bomb.powerDown();
                }
            }, item.getApplyDuration() / 1000000);
        } else if (item instanceof SpeedItem) {
            speed = DEFAULT_SPEED * 2;
            CommonVar.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    speed = DEFAULT_SPEED;
                }
            }, item.getApplyDuration() / 1000000);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
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
                throw new IllegalArgumentException("Bomber has not direction key event " + keyEvent.getCode());
        }
    }

    public Position move(Position position, DIRECTION direction) {
        Position newPosition;
        switch (direction) {
            case UP:
                if(position.getX() % Sprite.SCALED_SIZE == 0)
                    newPosition = new Position(position.getX(), position.getY() - speed);
                else if(position.getX() % Sprite.SCALED_SIZE <= 16)
                    newPosition = new Position(position.getX() - 1, position.getY());
                else
                    newPosition = new Position(position.getX() + 1, position.getY());
                break;
            case DOWN:
                if(position.getX() % Sprite.SCALED_SIZE == 0)
                    newPosition = new Position(position.getX(), position.getY() + speed);
                else if(position.getX() % Sprite.SCALED_SIZE <= 16)
                    newPosition = new Position(position.getX() - 1, position.getY());
                else
                    newPosition = new Position(position.getX() + 1, position.getY());
                break;
            case LEFT:
                if(position.getY() % Sprite.SCALED_SIZE == 0)
                    newPosition = new Position(position.getX() - speed, position.getY());
                else if(position.getY() % Sprite.SCALED_SIZE <= 16)
                    newPosition = new Position(position.getX(), position.getY() - 1);
                else
                    newPosition = new Position(position.getX(), position.getY() + 1);
                break;
            case RIGHT:
                if(position.getY() % Sprite.SCALED_SIZE == 0)
                    newPosition = new Position(position.getX() + speed, position.getY());
                else if(position.getY() % Sprite.SCALED_SIZE <= 16)
                    newPosition = new Position(position.getX(), position.getY() - 1);
                else
                    newPosition = new Position(position.getX(), position.getY() + 1);
                break;
            default:
                newPosition = position;
                break;
        }
        return newPosition;
    }
    @Override
    public void notify(KeyEvent keyEvent) {
        EventType<? extends Event> eventType = keyEvent.getEventType();
        if (KeyEvent.KEY_RELEASED.equals(eventType)) {
            if (keyEvent.getCode().equals(currentlyPressed)) {
                action = MOVING_ENTITY_ACTION.STOP;
                ///this.speed = 0;
            }
        } else if (KeyEvent.KEY_PRESSED.equals(eventType)) {
            currentlyPressed = keyEvent.getCode();
            switch (currentlyPressed) {
                case SPACE:
                    if (BombermanGame.bombQueue.size() < maxSpawnedBomb) {
                        BombermanGame.bombQueue.add(new Bomb(getBoardX(), getBoardY()));
                    }
                    break;
                default:
                    this.direction = getDirection(keyEvent);
                    action = MOVING_ENTITY_ACTION.MOVING;
                    ///this.speed = DEFAULT_SPEED;
            }
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
            BombermanGame.setGameStatus(GAME_STATUS.GAME_OVER);
        }
    }
}
