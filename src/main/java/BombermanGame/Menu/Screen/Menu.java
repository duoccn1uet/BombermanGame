package BombermanGame.Menu.Screen;

import BombermanGame.Entity.Entity;
import BombermanGame.Entity.Position;
import BombermanGame.Menu.ImageComponent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Menu {
    public Image image;
    public Position position;

    public Menu() {
        this.position = new Position(0, 0);
        this.image = ImageComponent.getFxImage("menu.png");
    }

    public void update() {

    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, position.getX(), position.getY());
    }
}
