package BombermanGame.Menu.Screen;

import BombermanGame.Entity.Position;
import BombermanGame.Menu.ImageComponent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Pause {
    public Image image;
    public Position position;

    public Pause() {
        this.position = new Position(0, 0);
        this.image = ImageComponent.getFxImage("pause.png");
    }

    public void update() {

    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, position.getX(), position.getY());
    }
}
