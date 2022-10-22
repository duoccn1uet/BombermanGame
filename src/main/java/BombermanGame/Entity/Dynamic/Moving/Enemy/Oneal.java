package BombermanGame.Entity.Dynamic.Moving.Enemy;

import BombermanGame.Entity.Entity;
import javafx.scene.image.Image;

public class Oneal extends Enemy {

    public Oneal(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public Oneal(int x, int y) {
        super(x, y);
    }

    @Override
    public void collide(Entity entity) {

    }
}
