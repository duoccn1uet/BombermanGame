package BombermanGame.Entity.Dynamic;

import BombermanGame.Entity.Dynamic.Moving.Bomber;
import BombermanGame.Entity.Dynamic.Moving.DIRECTION;
import BombermanGame.Entity.Dynamic.Moving.Enemy.Oneal;
import BombermanGame.Entity.Dynamic.Moving.MOVING_ENTITY_ACTION;
import BombermanGame.Entity.Dynamic.Moving.MovingEntity;
import BombermanGame.Entity.Dynamic.NotMoving.Bomb;
import BombermanGame.Entity.Dynamic.NotMoving.NotMovingEntity;
import BombermanGame.Sprite.Sprite;
import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;

public class Animation {
    /*
        Name of animation image format:
        name + '_' + state + number;
        I.e: minvo_left2
             minvo_dead1
     */
    private enum ANIMATION_TYPE {
        MOVING(0),
        NORMAL(4),
        DEAD(5);

        private final int value;
        ANIMATION_TYPE(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }
    private static final int LOOP_TIME = 10;
    private DynamicEntity entity;
    public static final int NUMBER_OF_TYPES = 6;
    private ArrayList<Image>[] animation = new ArrayList[NUMBER_OF_TYPES];
    private int[] listPointer = new int[NUMBER_OF_TYPES];
    private int[] countLoop = new int[NUMBER_OF_TYPES];
    private DIRECTION lastDirectionHasAnimation;
    private int animationLoop = 0;
    private int lastAnimationIndex = -1;

    Animation() {
        for (int i = 0; i < animation.length; ++i)
            animation[i] = new ArrayList<>();
    }

    public boolean finishCurrentAnimation() {
        return animationLoop > 0 && countLoop[lastAnimationIndex] == 0 && listPointer[lastAnimationIndex] == 0;
    }
    public int getAnimationLoop() {
        return animationLoop;
    }

    private void loadImageList(String entityName, String state, ArrayList<Image> moveList) {
        for (int i = 1; true; ++i) {
            String imageName = entityName + "_" + state + i + ".png";
            if (new File(Sprite.path + "/" + imageName).exists()) {
                moveList.add(Sprite.getFxImage(imageName));
            } else break;
        }
    }

    private void updateLastDirectionHasAnimation(DIRECTION direction) {
        if (!animation[direction.getValue()].isEmpty())
            lastDirectionHasAnimation = direction;
    }
    private void loadImageList(String entityName, ANIMATION_TYPE type) {
        switch (type) {
            case MOVING:
                for (DIRECTION direction : DIRECTION.values()) {
                    loadImageList(entityName, direction.name().toLowerCase(), animation[direction.getValue()]);
                    updateLastDirectionHasAnimation(direction);
                }
                break;
            case DEAD:
                loadImageList(entityName, "dead", animation[type.getValue()]);
                break;
            case NORMAL:
                animation[type.getValue()].add(Sprite.getFxImage(entityName + ".png"));
                loadImageList(entityName, "", animation[type.getValue()]);
                break;
        }
    }

    public void load(DynamicEntity entity) {
        try {
            this.entity = entity;
            String entityName = entity.getEntityName(true);
            if (entity instanceof MovingEntity) {
                loadImageList(entityName, ANIMATION_TYPE.DEAD);
                loadImageList(entityName, ANIMATION_TYPE.MOVING);
            } else if (entity instanceof NotMovingEntity) {
                loadImageList(entityName, ANIMATION_TYPE.NORMAL);
            } else {
                throw new Exception("Can not load animation for nondynamic entity");
            }
        }
        catch (Exception e) {
            System.out.println("Error occurred when load animation for " + entity.getEntityName(true));
            e.printStackTrace();
        }
    }

    private Image getCurrentImage(int i) {
        lastAnimationIndex = i;
        Image res = animation[i].get(listPointer[i]);
        if (++countLoop[i] == LOOP_TIME) {
            countLoop[i] = 0;
            if (++listPointer[i] == animation[i].size()) {
                listPointer[i] = 0;
                ++animationLoop;
            }
        }
        return res;
    }
    public Image getCurrentImage() {
        if (entity instanceof NotMovingEntity) {
            return getCurrentImage(ANIMATION_TYPE.NORMAL.getValue());
        } else if (entity instanceof MovingEntity) {
            MovingEntity tEntity = (MovingEntity) entity;
            switch (tEntity.getAction()) {
                case DEAD:
                    return getCurrentImage(ANIMATION_TYPE.DEAD.getValue());
                case STOP:
                    DIRECTION entityDirection = tEntity.getDirection();
                    if (animation[entityDirection.getValue()].isEmpty()) {
                        return animation[lastDirectionHasAnimation.getValue()].get(0);
                    }
                    lastDirectionHasAnimation = entityDirection;
                    return animation[entityDirection.getValue()].get(0);
                default: /// Moving
                    entityDirection = tEntity.getDirection();
                    if (animation[entityDirection.getValue()].isEmpty()) {
                        return getCurrentImage(lastDirectionHasAnimation.getValue());
                    }
                    lastDirectionHasAnimation = entityDirection;
                    return getCurrentImage(entityDirection.getValue());
            }
        }
        return null;
    }
}
