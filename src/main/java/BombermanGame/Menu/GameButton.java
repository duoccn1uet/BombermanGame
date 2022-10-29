package BombermanGame.Menu;

import BombermanGame.Entity.Position;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class GameButton {
    public Image[] image = new Image[2];
    public Position position;
    public static int BUTTON_STATUS = 0;
    public static int BUTTON_WIDTH = 200;
    public static int BUTTON_HEIGHT = 65;

    public GameButton(int x, int y, String image0, String image1) {
        this.position = new Position(x, y);
        // default
        this.image[0] = ImageComponent.getFxImage(image0);
        // hovered
        this.image[1] = ImageComponent.getFxImage(image1);
    }

    public void hovered(Position cursor) {
        if(onButton(cursor)) {
            BUTTON_STATUS = 1;
        } else {
            BUTTON_STATUS = 0;
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

    public void render(GraphicsContext gc) {
        gc.drawImage(image[BUTTON_STATUS], position.getX(), position.getY());
    }
}
