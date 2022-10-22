package BombermanGame.Entity.Dynamic.Moving;

import BombermanGame.Entity.Dynamic.Moving.Enemy.Enemy;
import BombermanGame.Entity.Dynamic.NotMoving.Brick;
import BombermanGame.Entity.Entity;
import BombermanGame.Entity.Still.Wall;
import javafx.scene.image.Image;

public class Bomber extends MovingEntity {
    public Bomber(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public Bomber(int x, int y) {
        super(x, y);
    }

    @Override
    public void update() {

    }

    @Override
    public void collide(Entity entity) {
        if(entity instanceof Wall)
            collide((Wall) entity);
        else if(entity instanceof Brick)
            collide((Brick) entity);
        else if(entity instanceof Enemy)
            collide((Enemy) entity);
    }

    protected void collide(Wall wall) {
        if(last != null) {
            position = last;
        }
        last = null;
    }

    protected void collide(Brick brick) {
        if(last != null) {
            position = last;
        }
        last = null;
    }

    protected void collide(Enemy enemy) {
        if(!isDead) {
            isDead = true;
            speed = 0;
        }
    }
}
