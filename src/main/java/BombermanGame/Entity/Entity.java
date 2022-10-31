package BombermanGame.Entity;

import BombermanGame.BombermanGame;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import BombermanGame.Sprite.*;

import java.io.FileInputStream;

import static java.lang.Character.isUpperCase;

public abstract class Entity {
    protected Position position;

    protected Image img;

    protected Entity() {

    }

    public int getBoardX() {
        return getX() / Sprite.SCALED_SIZE;
    }

    public int getBoardY() {
        return getY() / Sprite.SCALED_SIZE;
    }

    public void setBoardX(int x) {
        setX(x * Sprite.SCALED_SIZE);
    }

    public void setBoardY(int y) {
        setY(y * Sprite.SCALED_SIZE);
    }
    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public void setX(int x) {
        position.setX(x);
    }

    public void setY(int y) {
        position.setY(y);
    }
    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    //Khởi tạo đối tượng, chuyển từ tọa độ đơn vị sang tọa độ trong canvas
    public Entity(int xUnit, int yUnit, Image img) {
        this.position = new Position(xUnit * Sprite.SCALED_SIZE, yUnit * Sprite.SCALED_SIZE);
        this.img = img;
    }

    public Entity(int x, int y) {
        this.position = new Position(x * Sprite.SCALED_SIZE, y * Sprite.SCALED_SIZE);
        String className = this.getClass().getSimpleName();
        className = className.toLowerCase();
        try {
            this.img = Sprite.getFxImage(className + ".png");
        }
        catch (Exception e) {
            System.out.println("Failed to load " + className);
        }
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(position.getX(), position.getY(), Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);
    }

    public boolean isColliding(Entity entity) {
        if(this == entity)
            return false;
        return this.getBoundary().intersects(entity.getBoundary());
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(img, position.getX(), position.getY());
    }


    public String getEntityName(boolean toLowerCase) {
        if (toLowerCase)
            return this.getClass().getSimpleName().toLowerCase();
        return this.getClass().getSimpleName();
    }
    public abstract void update();

    // Collision
    public abstract void collide(Entity entity);
}
