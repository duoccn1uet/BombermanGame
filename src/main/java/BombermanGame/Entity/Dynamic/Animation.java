package BombermanGame.Entity.Dynamic;

import BombermanGame.Entity.Dynamic.Moving.MovingEntity;
import BombermanGame.Entity.Dynamic.NotMoving.NotMovingEntity;
import BombermanGame.Entity.Entity;
import BombermanGame.Sprite.Sprite;
import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;

public class Animation {
    /*
        Direction string format:
        name + '_' + direction + number;
        I.e: minvo_left2
     */
    private static final int LOOP_TIME = 15;
    private int moveListPointer = 0;
    private int countLoop = 0;
    private Entity entity;
    private ArrayList<Image> normal = new ArrayList<>();
    private ArrayList<Image> upMove = new ArrayList<>();
    private ArrayList<Image> rightMove = new ArrayList<>();
    private ArrayList<Image> downMove = new ArrayList<>();
    private ArrayList<Image> leftMove = new ArrayList<>();

    private void loadImageList(String entityName, String direction, ArrayList<Image> moveList) {
        for (int i = 1; true; ++i) {
            String imageName = entityName + "_" + direction + i + ".png";
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
                loadImageList(entityName, "up", upMove);
                loadImageList(entityName, "right", rightMove);
                loadImageList(entityName, "down", downMove);
                loadImageList(entityName, "left", leftMove);
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
            return null;
        }
    }
}
