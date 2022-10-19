package BombermanGame.Map;

import BombermanGame.Entity.Dynamic.DynamicEntity;
import BombermanGame.Entity.Dynamic.Moving.Bomber;
import BombermanGame.Entity.Dynamic.NotMoving.Brick;
import BombermanGame.Entity.Entity;
import BombermanGame.Entity.Still.StillEntity;
import BombermanGame.Entity.Still.Wall;
import BombermanGame.Sprite.Sprite;
import javafx.scene.canvas.GraphicsContext;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Map {

    private static int width = 20;
    private static int height = 15;

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        Map.width = width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        Map.height = height;
    }


}
