package BombermanGame.Entity.Dynamic;

import BombermanGame.Entity.Dynamic.Moving.DIRECTION;
import BombermanGame.Entity.Dynamic.Moving.MOVING_ENTITY_ACTION;
import BombermanGame.Entity.Dynamic.Moving.MovingEntity;
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
    private static final int LOOP_TIME = 15;
    private int moveListPointer = 0;
    private int countLoop = 0;
    private DynamicEntity entity;
    private ArrayList<Image> normal = new ArrayList<>();
    private ArrayList<Image>[] directionMoves = new ArrayList[DIRECTION.size()];
    private ArrayList<Image> dead = new ArrayList<>();

    Animation() {
        for (int i = 0; i < directionMoves.length; ++i)
            directionMoves[i] = new ArrayList<>();
    }
    private void loadImageList(String entityName, String state, ArrayList<Image> moveList) {
        for (int i = 1; true; ++i) {
            String imageName = entityName + "_" + state + i + ".png";
            if (new File(Sprite.path + "/" + imageName).exists()) {
                moveList.add(Sprite.getFxImage(imageName));
            } else break;
        }
    }
    public void load(DynamicEntity entity) {
        try {
            this.entity = entity;
            if (entity instanceof MovingEntity) {
                String entityName = entity.getClass().getSimpleName().toLowerCase();
//                loadImageList(entityName, "up", upMove);
//                loadImageList(entityName, "right", rightMove);
//                loadImageList(entityName, "down", downMove);
//                loadImageList(entityName, "left", leftMove);
                for (DIRECTION direction : DIRECTION.values()) {
                    loadImageList(entityName, direction.name().toLowerCase(), directionMoves[direction.getValue()]);
                }
                loadImageList(entityName, "dead", dead);
            } else if (entity instanceof NotMovingEntity) {
                String entityName = entity.getClass().getSimpleName().toLowerCase();
                normal.add(Sprite.getFxImage(entityName + ".png"));
                loadImageList(entityName, "", normal);
            } else {
                throw new Exception("Can not load animation for nondynamic entity");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Image getCurrentImage(ArrayList<Image> moveList) {
        Image res = moveList.get(moveListPointer);
        if (++countLoop == LOOP_TIME) {
            countLoop = 0;
            if (++moveListPointer == moveList.size())
                moveListPointer = 0;
        }
        return res;
    }
    public Image getCurrentImage() {
        if (entity instanceof NotMovingEntity) {
            return getCurrentImage(normal);
        } else {
            MovingEntity tEntity = (MovingEntity) entity;
            switch (tEntity.getAction()) {
                case DEAD:
                    return getCurrentImage(dead);
                case STOP:
                    DIRECTION entityDirection = tEntity.getDirection();
                    return directionMoves[entityDirection.getValue()].get(0);
                default: /// Moving
                    entityDirection = tEntity.getDirection();
                    return getCurrentImage(directionMoves[entityDirection.getValue()]);
            }
        }
    }
}
