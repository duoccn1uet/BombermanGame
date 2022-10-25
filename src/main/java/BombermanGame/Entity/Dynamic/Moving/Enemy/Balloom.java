package BombermanGame.Entity.Dynamic.Moving.Enemy;

import javafx.scene.image.Image;

import java.util.Random;

public class Balloom extends Enemy {

    public Balloom(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        speed = 1;
    }

    public Balloom(int x, int y) {
        super(x, y);
        speed = 1;
    }

    @Override
    public void update() {
        super.update();
    }

}
