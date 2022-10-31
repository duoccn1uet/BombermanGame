package BombermanGame.Entity.Dynamic.NotMoving;

import BombermanGame.BombermanGame;
import BombermanGame.Entity.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Brick extends NotMovingEntity {

    private static class brick_exploded extends Explosion {

        public brick_exploded(int x, int y) {
            super(x, y);
        }
    }
    private brick_exploded explodedBrick;
    private boolean isDetonated = false;
    ///private long startSpawnTime = 0;
    public Brick(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    protected void setDefaultSpecifications(Object... specifications) {

    }

    public void detonate() {
        //startSpawnTime = BombermanGame.getTime();
        isDetonated = true;
        explodedBrick = new brick_exploded(getBoardX(), getBoardY());
    }

    public Brick(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean isVanished() {
        return explodedBrick != null && explodedBrick.isVanished();
    }

    @Override
    public void update() {
        if (isDetonated) {
            explodedBrick.update();
        } else {
            super.update();
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (isDetonated) {
            explodedBrick.render(gc);
        } else {
            super.render(gc);
        }
    }

    @Override
    public void collide(Entity entity) {

    }
}
