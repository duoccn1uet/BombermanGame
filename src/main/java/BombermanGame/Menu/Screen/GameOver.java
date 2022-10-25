package BombermanGame.Menu.Screen;

import BombermanGame.Entity.Position;
import BombermanGame.Menu.ImageComponent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;

public class GameOver {
    public Image image;
    public Position position;

    public GameOver() {
        this.position = new Position(0, 0);
        this.image = ImageComponent.getFxImage("game_over.png");
    }

    public void update() {

    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, position.getX(), position.getY());
    }
}
