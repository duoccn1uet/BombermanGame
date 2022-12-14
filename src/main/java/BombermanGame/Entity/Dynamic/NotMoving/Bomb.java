package BombermanGame.Entity.Dynamic.NotMoving;

import BombermanGame.BombermanGame;
import BombermanGame.Entity.Dynamic.DynamicEntity;
import BombermanGame.Entity.Dynamic.Moving.DIRECTION;
import BombermanGame.Entity.Dynamic.Moving.Enemy.Enemy;
import BombermanGame.Entity.Dynamic.Moving.MOVING_ENTITY_ACTION;
import BombermanGame.Entity.Dynamic.Moving.MovingEntity;
import BombermanGame.Entity.Entity;
import BombermanGame.Entity.Still.StillEntity;
import BombermanGame.Entity.Still.Wall;
import BombermanGame.Sound.Sound;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class Bomb extends NotMovingEntity {
    private static class bomb_exploded extends Explosion {
        public bomb_exploded(int x, int y) {
            super(x, y);
        }
    }
    private static class explosion_horizontal extends Explosion {
        public explosion_horizontal(int x, int y) {
            super(x, y);
        }

    }
    private static class explosion_horizontal_left_last extends Explosion {
        public explosion_horizontal_left_last(int x, int y) {
            super(x, y);
        }

    }
    private static class explosion_horizontal_right_last extends Explosion {
        public explosion_horizontal_right_last(int x, int y) {
            super(x, y);
        }
    }
    private static class explosion_vertical extends Explosion {
        public explosion_vertical(int x, int y) {
            super(x, y);
        }
    }
    private static class explosion_vertical_down_last extends Explosion {
        public explosion_vertical_down_last(int x, int y) {
            super(x, y);
        }
    }
    private static class explosion_vertical_top_last extends Explosion {
        public explosion_vertical_top_last(int x, int y) {
            super(x, y);
        }
    }

    private bomb_exploded bE;
    private ArrayList<Explosion>[] explosions = new ArrayList[DIRECTION.size()];

    private static boolean isPowerUp = false;
    private static final int DEFAULT_EXPLOSION_LENGTH  = 2;
    private static final int POWER_UP_EXPLOSION_LENGTH  = 4;
    public static final long waitForExplosion = (long) 3e9;

    private final int explosionLength = isPowerUp ? POWER_UP_EXPLOSION_LENGTH : DEFAULT_EXPLOSION_LENGTH;
    private boolean isDetonated = false;
    private long startSpawnTime = 0;

    public Sound bombTimerSound = new Sound(Sound.FILE.BOMB_TIMER.toString());
    public Sound explosionSound = new Sound(Sound.FILE.EXPLOSION.toString());
    public Bomb(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        spawn();
    }
    public Bomb(int x, int y) {
        super(x, y);
        spawn();
    }

    @Override
    public boolean isVanished() {
        return bE != null && bE.isVanished();
    }

    @Override
    protected void setDefaultSpecifications(Object... specifications) {

    }
    @Override
    public void collide(Entity entity) {

    }

    public void spawn() {
        startSpawnTime = BombermanGame.getTime();
        bombTimerSound.play();
    }
    public static void powerUp() {
        isPowerUp = true;
    }

    public static void powerDown() {
        isPowerUp = false;
    }
    private ArrayList<StillEntity> sl = BombermanGame.getStillEntities();
    private ArrayList<DynamicEntity> dl = BombermanGame.getDynamicEntities();
    private boolean checkCollision(Explosion explosion) {
        for (Entity barrier : sl) {
            if (barrier instanceof Wall && barrier.isColliding(explosion))
                return true;
        }
        for (Entity barrier : dl)
            if (barrier.isColliding(explosion)) {
            if (barrier instanceof Brick) {
                ((Brick) barrier).detonate();
                return true;
            } else if (barrier instanceof MovingEntity) {
                if (barrier instanceof Enemy && ((Enemy) barrier).getAction() != MOVING_ENTITY_ACTION.DEAD)
                    BombermanGame.score += ((Enemy) barrier).getBonusScore();
                ((MovingEntity) barrier).setAction(MOVING_ENTITY_ACTION.DEAD);
            }
        }
        for (Bomb other : BombermanGame.bombQueue)
            if (other != this && !other.isDetonated && other.isColliding(explosion))
                other.detonate();
        return false;
    }

    private void detonate() {
        isDetonated = true;
        int x = getBoardX();
        int y = getBoardY();
        bE = new bomb_exploded(x, y);
        checkCollision(bE);
        for (DIRECTION direction : DIRECTION.values()) {
            int i = direction.getValue();
            int signX = direction.signXY()[0];
            int signY = direction.signXY()[1];
            explosions[i] = new ArrayList<>();
            for (int j = 1; j <= explosionLength; ++j) {
                int eX = x + signX * j;
                int eY = y + signY * j;
                Explosion e;
                switch (direction) {
                    case UP:
                        if (j < explosionLength)
                            e = new explosion_vertical(eX, eY);
                        else
                            e = new explosion_vertical_top_last(eX, eY);
                        break;
                    case RIGHT:
                        if (j < explosionLength)
                            e = new explosion_horizontal(eX, eY);
                        else
                            e = new explosion_horizontal_right_last(eX, eY);
                        break;
                    case DOWN:
                        if (j < explosionLength)
                            e = new explosion_vertical(eX, eY);
                        else
                            e = new explosion_vertical_down_last(eX, eY);
                        break;
                    default:
                        if (j < explosionLength)
                            e = new explosion_horizontal(eX, eY);
                        else
                            e = new explosion_horizontal_left_last(eX, eY);
                        break;
                }
                if (checkCollision(e))
                    break;
                explosions[i].add(e);
            }
        }
        explosionSound.play();
    }

    @Override
    public void update() {
        if (!isDetonated && BombermanGame.getTime() - startSpawnTime >= waitForExplosion)
            detonate();
        if (!isDetonated) {
            super.update();
        } else {
            bE.update();
            checkCollision(bE);
            for (ArrayList<Explosion> explosionList : explosions)
                for (Explosion explosion : explosionList) {
                    explosion.update();
                    checkCollision(explosion);
                }
        }
    }
    @Override
    public void render(GraphicsContext gc) {
        if (!isDetonated) {
            super.render(gc);
        } else {
            bE.render(gc);
            for (ArrayList<Explosion> explosionList : explosions)
                for (Explosion explosion : explosionList)
                    explosion.render(gc);
        }
    }
}
