package BombermanGame.Menu;

import BombermanGame.Entity.Position;
import javafx.scene.image.Image;

public class GameButton {
    public Image image;
    public Position position;

    public GameButton(int x, int y, String image) {
        this.position = new Position(x, y);
        this.image = ImageComponent.getFxImage(image);
    }
}
