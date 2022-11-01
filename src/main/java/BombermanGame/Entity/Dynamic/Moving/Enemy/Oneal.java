package BombermanGame.Entity.Dynamic.Moving.Enemy;

import BombermanGame.BombermanGame;
import BombermanGame.Entity.Dynamic.Moving.DIRECTION;
import BombermanGame.Entity.Dynamic.Moving.MOVING_ENTITY_ACTION;
import BombermanGame.Entity.Dynamic.NotMoving.Bomb;
import BombermanGame.Entity.Dynamic.NotMoving.Brick;
import BombermanGame.Entity.Entity;
import BombermanGame.Entity.Position;
import BombermanGame.Entity.Still.Grass;
import BombermanGame.Entity.Still.Wall;
import BombermanGame.Sprite.Sprite;
import BombermanGame.TaskHandler.Pair;
import javafx.scene.image.Image;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class Oneal extends Enemy {

    public static final DIRECTION DEFAULT_DIRECTION = DIRECTION.RIGHT;
    public static final MOVING_ENTITY_ACTION DEFAULT_ACTION = MOVING_ENTITY_ACTION.MOVING;
    public static final int DEFAULT_SPEED = 1;
    private static final int DEFAULT_BONUS_SCORE = 50;
    private boolean isChasing = false;

    @Override
    protected void initBonusScore() {
        bonusScore = DEFAULT_BONUS_SCORE;
    }

    public Oneal(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        setDefaultSpecifications(DEFAULT_DIRECTION, DEFAULT_ACTION, DEFAULT_SPEED);
    }


    public Oneal(int x, int y) {
        super(x, y);
        setDefaultSpecifications(DEFAULT_DIRECTION, DEFAULT_ACTION, DEFAULT_SPEED);
    }

    @Override
    public Position move() {
        Position newPosition;

        if(position.getX() % 64 == 32 && position.getY() % 64 == 32) {
            Random random = new Random();
            int rnd = random.nextInt(100);

            if(rnd < 30) {
                isChasing = true;
            } else {
                isChasing = false;
            }
        }

        if(isChasing) {
            newPosition = chasing();
        } else {
            newPosition = super.move();
        }

        return newPosition;
    }

    private Position chasing() {
        Position endPosition;

        int[][] board = new int[BombermanGame.WIDTH][];
        for(int i = 0; i < BombermanGame.WIDTH; i ++) {
            board[i] = new int[BombermanGame.HEIGHT];
            for(int j = 0; j < BombermanGame.HEIGHT; j ++)
                board[i][j] = 9999;
        }

        Queue<Pair<Integer, Integer>> q = new LinkedList<>();

        // get Bomber position
        board[BombermanGame.bomber.getBoardX()][BombermanGame.bomber.getBoardY()] = 0;
        q.add(new Pair<>(BombermanGame.bomber.getBoardX(), BombermanGame.bomber.getBoardY()));
        // get brick and wall position
        for(Entity entity : BombermanGame.getStillEntities()) {
            if(entity instanceof Grass)
                continue;
            board[entity.getBoardX()][entity.getBoardY()] = -1;
        }
        for(Entity entity : BombermanGame.getDynamicEntities()) {
            if(entity instanceof Brick)
                board[entity.getBoardX()][entity.getBoardY()] = - 1;
        }
        for(Bomb bomb : BombermanGame.bombQueue) {
            board[bomb.getBoardX()][bomb.getBoardY()] = -1;
        }

        // calculate the path
        while(!q.isEmpty()) {
            int x = q.peek().first;
            int y = q.peek().second;
            q.remove(q.peek());

            if(board[x - 1][y] != -1 && board[x][y] + 1 < board[x - 1][y]) {
                board[x - 1][y] = board[x][y] + 1;
                q.add(new Pair<>(x - 1, y));
            }
            if(board[x + 1][y] != -1 && board[x][y] + 1 < board[x + 1][y]) {
                board[x + 1][y] = board[x][y] + 1;
                q.add(new Pair<>(x + 1, y));
            }
            if(board[x][y - 1] != -1 && board[x][y] + 1 < board[x][y - 1]) {
                board[x][y - 1] = board[x][y] + 1;
                q.add(new Pair<>(x, y - 1));
            }
            if(board[x][y + 1] != -1 && board[x][y] + 1 < board[x][y + 1]) {
                board[x][y + 1] = board[x][y] + 1;
                q.add(new Pair<>(x, y + 1));
            }
        }

        int x = this.getBoardX();
        int y = this.getBoardY();

        if(board[x][y] == board[x - 1][y] + 1 && board[x - 1][y] != -1) {
            if(position.getY() % Sprite.SCALED_SIZE == 0)
                endPosition = position.left(speed);
            else if(position.getY() % Sprite.SCALED_SIZE < 16)
                endPosition = position.up(1);
            else
                endPosition = position.down(1);
            setDirection(DIRECTION.LEFT);
        } else if(board[x][y] == board[x + 1][y] + 1 && board[x + 1][y] != -1) {
            if(position.getY() % Sprite.SCALED_SIZE == 0)
                endPosition = position.right(speed);
            else if(position.getY() % Sprite.SCALED_SIZE < 16)
                endPosition = position.up(1);
            else
                endPosition = position.down(1);
            setDirection(DIRECTION.RIGHT);
        } else if(board[x][y] == board[x][y - 1] + 1 && board[x][y - 1] != -1) {
            if(position.getX() % Sprite.SCALED_SIZE == 0)
                endPosition = position.up(speed);
            else if(position.getX() % Sprite.SCALED_SIZE < 16)
                endPosition = position.left(1);
            else
                endPosition = position.right(1);
            setDirection(DIRECTION.UP);
        } else if(board[x][y] == board[x][y + 1] + 1 && board[x][y + 1] != -1) {
            if(position.getX() % Sprite.SCALED_SIZE == 0)
                endPosition = position.down(speed);
            else if(position.getX() % Sprite.SCALED_SIZE < 16)
                endPosition = position.left(1);
            else
                endPosition = position.right(1);
            setDirection(DIRECTION.DOWN);
        } else {
            endPosition = super.move();
        }

        return endPosition;
    }

    @Override
    protected void setDefaultSpecifications(Object... specifications) {
        direction = (DIRECTION) specifications[0];
        setAction((MOVING_ENTITY_ACTION) specifications[1]);
        speed = (int) specifications[2];
    }

    @Override
    public void update() {
        super.update();
    }
}
