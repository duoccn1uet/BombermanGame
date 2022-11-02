package BombermanGame.Menu;

import BombermanGame.Entity.Position;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.File;

public class GameButton {
    public Image[] image = new Image[3];
    public Position position;
    public int BUTTON_STATUS = 0;
    public static int BUTTON_WIDTH = 200;
    public static int BUTTON_HEIGHT = 65;

    public GameButton(int x, int y, String p1, String p2) {
        this.position = new Position(x, y);
        // default
        this.image[0] = ImageComponent.getFxImage(p1);
        // hovered
        this.image[1] = ImageComponent.getFxImage(p2);
        // clicked
        this.image[2] = ImageComponent.getFxImage(p1);
    }

    public void hovered(Position cursor) {
        if(onButton(cursor)) {
            this.BUTTON_STATUS = 1;
        } else {
            this.BUTTON_STATUS = 0;
        }
    }

    public boolean clicked(Position cursor) {
        return onButton(cursor);
    }

    private boolean onButton(Position cursor) {
        int lx = position.getX();
        int rx = position.getX() + BUTTON_WIDTH;
        int ly = position.getY();
        int ry = position.getY() + BUTTON_HEIGHT;

        return lx <= cursor.getX() && cursor.getX() <= rx
            && ly <= cursor.getY() && cursor.getY() <= ry;
    }

    public int getBUTTON_STATUS() {
        return BUTTON_STATUS;
    }

    public void setBUTTON_STATUS(int BUTTON_STATUS) {
        this.BUTTON_STATUS = BUTTON_STATUS;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image[BUTTON_STATUS], position.getX(), position.getY());
    }
}
