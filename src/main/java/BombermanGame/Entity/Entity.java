package BombermanGame.Entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import BombermanGame.Sprite.*;

import java.io.FileInputStream;

import static java.lang.Character.isUpperCase;

public abstract class Entity {
    //Tọa độ X tính từ góc trái trên trong Canvas
    protected int x;

    //Tọa độ Y tính từ góc trái trên trong Canvas
    protected int y;

    protected Image img;

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    //Khởi tạo đối tượng, chuyển từ tọa độ đơn vị sang tọa độ trong canvas
    public Entity(int xUnit, int yUnit, Image img) {
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.img = img;
    }

    public Entity(int x, int y) {
        this.x = x * Sprite.SCALED_SIZE;
        this.y = y * Sprite.SCALED_SIZE;
        String className = this.getClass().getSimpleName();
        className = className.toLowerCase();
        try {
            this.img = Sprite.getFxImage(className + ".png");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(img, x, y);
    }

    public String getEntityName(boolean toLowerCase) {
        if (toLowerCase)
            return this.getClass().getSimpleName().toLowerCase();
        return this.getClass().getSimpleName();
    }
    public abstract void update();
}
