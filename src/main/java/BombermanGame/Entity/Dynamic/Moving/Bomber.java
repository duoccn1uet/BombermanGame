package BombermanGame.Entity.Dynamic.Moving;


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
import BombermanGame.Sound.Sound;
import BombermanGame.TaskHandler.KeyEventHandler.KeyEventListener;
import BombermanGame.Sprite.Sprite;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import static javafx.scene.input.KeyCode.*;

public class Bomber extends MovingEntity implements KeyEventListener {
    public static final DIRECTION DEFAULT_DIRECTION = DIRECTION.RIGHT;
    public static final MOVING_ENTITY_ACTION DEFAULT_ACTION = MOVING_ENTITY_ACTION.STOP;
    public static final int DEFAULT_SPEED = 2;
    public boolean onBomb = true;
    private int[] countApplyingItems = new int[Item.ITEM_TYPE.size()];
    private final List<KeyCode> keyCodes = Arrays.asList(A, D, W, S, SPACE, ESCAPE);
    private KeyCode currentlyPressed;
    private int maxSpawnedBomb = 1;
    private Sound applyItemSound = new Sound(Sound.FILE.APPLY_ITEM.toString());
    private Sound deadSound = new Sound(Sound.FILE.BOMBER_DEAD.toString());

    public int getMaxSpawnedBomb() {
        return maxSpawnedBomb;
    }

    @Override
    protected void setDefaultSpecifications(Object... specifications) {
        direction = (DIRECTION) specifications[0];
        setAction((MOVING_ENTITY_ACTION) specifications[1]);
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
        if (isVanished()) {
            BombermanGame.setGameStatus(GAME_STATUS.GAME_OVER);
        }
        if(getAction() == MOVING_ENTITY_ACTION.MOVING) {
            this.last = new Position(position.getX(), position.getY());
            position = move(position, direction);
        }
        for (Item item : BombermanGame.itemList)
            if (!item.isInsideBrick() && isColliding(item)) {
                applyItem(item);
                BombermanGame.score += item.getBonusScore();
                BombermanGame.scoreBoard.addItemProperty(item);
                BombermanGame.itemList.remove(item);
                break;
            }
//        for later bomberman update: when burned to dead -=> Game over screen
//        if(action == MOVING_ENTITY_ACTION.DEAD) {
//            BombermanGame.setGameStatus(GAME_STATUS.GAME_OVER);
//        }
    }

    @Override
    public void setAction(MOVING_ENTITY_ACTION action) {
        if (action == MOVING_ENTITY_ACTION.DEAD)
            deadSound.play();
        super.setAction(action);
    }

    private void applyItem(Item item) {
        applyItemSound.play();
        int type = item.getType().getValue();
        if (item instanceof BombItem) {
            ++maxSpawnedBomb;
        } else if (item instanceof FlameItem) {
            Bomb.powerUp();
            ++countApplyingItems[type];
            CommonVar.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (--countApplyingItems[type] == 0)
                        Bomb.powerDown();
                }
            }, item.getApplyDuration() / 1000000);
        } else if (item instanceof SpeedItem) {
            speed = DEFAULT_SPEED * 2;
            ++countApplyingItems[type];
            CommonVar.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (--countApplyingItems[type] == 0)
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
        return super.move();
    }
    @Override
    public void notify(KeyEvent keyEvent) {
        if (getAction() == MOVING_ENTITY_ACTION.DEAD)
            return;
        EventType<? extends Event> eventType = keyEvent.getEventType();
        if (KeyEvent.KEY_RELEASED.equals(eventType)) {
            if (keyEvent.getCode().equals(currentlyPressed)) {
                setAction(MOVING_ENTITY_ACTION.STOP);
                ///this.speed = 0;
            }
        } else if (KeyEvent.KEY_PRESSED.equals(eventType)) {
            currentlyPressed = keyEvent.getCode();
            switch (currentlyPressed) {
                case SPACE:
                    if (BombermanGame.bombQueue.size() < maxSpawnedBomb) {
                        int tmpX = getBoardX();
                        int tmpY = getBoardY();
                        BombermanGame.bombQueue.add(new Bomb(tmpX, tmpY));
                        onBomb = true;
                    }
                    break;
                case ESCAPE:
                    if(BombermanGame.getGameStatus() == GAME_STATUS.RUNNING)
                        BombermanGame.setGameStatus(GAME_STATUS.PAUSED);
                    else
                        BombermanGame.setGameStatus(GAME_STATUS.RUNNING);
                    break;
                default:
                    this.direction = getDirection(keyEvent);
                    setAction(MOVING_ENTITY_ACTION.MOVING);
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
        else if(entity instanceof Bomb)
            collide((Bomb) entity);
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
        if(getAction() != MOVING_ENTITY_ACTION.DEAD) {
            setAction(MOVING_ENTITY_ACTION.DEAD);
            speed = 0;
        }
    }

    protected void collide(Bomb bomb) {
        if(onBomb)
            return;

        if(last != null)
            position = last;
        last = null;
    }
}
