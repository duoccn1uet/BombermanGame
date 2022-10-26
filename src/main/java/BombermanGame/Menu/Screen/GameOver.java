package BombermanGame.Menu.Screen;

import BombermanGame.Entity.Position;
import BombermanGame.Menu.GameButton;
import BombermanGame.Menu.ImageComponent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;

public class GameOver {
    public Image image;
    public Position position;
    public GameButton retry;
    public GameButton back;

    public GameOver() {
        this.position = new Position(0, 0);
        this.image = ImageComponent.getFxImage("game_over.png");

        this.retry = new GameButton(300, 275, "pause_retry.png");
        this.back = new GameButton(300, 360, "pause_back.png");
    }

    public void update() {

    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, position.getX(), position.getY());
        retry.render(gc);
        back.render(gc);
    }
}
