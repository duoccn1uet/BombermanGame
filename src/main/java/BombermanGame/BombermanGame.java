package BombermanGame;

import BombermanGame.Entity.Dynamic.DynamicEntity;
import BombermanGame.Entity.Dynamic.Moving.Bomber;
import BombermanGame.Entity.Dynamic.Moving.Enemy.Balloom;
import BombermanGame.Entity.Dynamic.Moving.Enemy.Doll;
import BombermanGame.Entity.Dynamic.Moving.Enemy.Kondoria;
import BombermanGame.Entity.Dynamic.Moving.Enemy.Oneal;
import BombermanGame.Entity.Dynamic.NotMoving.Bomb;
import BombermanGame.Entity.Dynamic.NotMoving.Brick;
import BombermanGame.Entity.Entity;
import BombermanGame.Entity.Still.Grass;
import BombermanGame.Entity.Still.Item.Item;
import BombermanGame.Entity.Still.StillEntity;
import BombermanGame.Entity.Still.Wall;
import BombermanGame.Sound.Sound;
import BombermanGame.TaskHandler.KeyEventHandler.KeyEventHandler;
import BombermanGame.TaskHandler.KeyEventHandler.KeyEventHandlerImpl;
import BombermanGame.Menu.Screen.GameOver;
import BombermanGame.Menu.Screen.Menu;
import BombermanGame.Menu.Screen.Pause;
import BombermanGame.TaskHandler.MouseEventHandler.MouseEventHandler;
import BombermanGame.TaskHandler.MouseEventHandler.MouseEventHandlerImpl;
import BombermanGame.ScoreBoard.ScoreBoard;
import BombermanGame.Sprite.Sprite;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class BombermanGame extends Application {
    public static int WIDTH = 25;
    public static int HEIGHT = 15;
    public static int R_WIDTH;
    public static int R_HEIGHT;
    public static Queue<Bomb> bombQueue = new LinkedList<>();
    public static final int NUMBER_OF_LEVELS = 5;
    private int level = 3;
    private String path;
    private static ArrayList<DynamicEntity> dynamicEntities = new ArrayList<>();
    private static ArrayList<StillEntity> stillEntities = new ArrayList<>();
    private static ArrayList<Grass> grasses = new ArrayList<>();
    public static Bomber bomber;/// = new Bomber();
    public static Group root;
    private Scene scene;
    private Canvas canvas;
    private static GraphicsContext gc;
    private Sound screenSound;
    private TimerTask countDownTask;
    private static Sound gameOverSound;

    public static ArrayList<DynamicEntity> getDynamicEntities() {
        return dynamicEntities;
    }

    public static ArrayList<StillEntity> getStillEntities() {
        return stillEntities;
    }

    // screen
    private Menu menu = new Menu();
    private Pause pause = new Pause();
    private GameOver gameOver = new GameOver();
    public static GAME_STATUS gameStatus = GAME_STATUS.MENU;
    private static final int MAX_ITEMS_PER_LEVEL = 10;
    private static final int MIN_ITEMS_PER_LEVEL = 4;
    private static final int[] ITEMS_PER_LEVEL = new int[NUMBER_OF_LEVELS];
    private static final int[] TIME_PER_LEVEL = {300, 300, 300, 300};
    private static int remainingTime;
    public static int score = 0;
    public static int getRemainingTime() {
        return remainingTime;
    }

    public static ArrayList<Item> itemList = new ArrayList<>();
    // handler
    KeyEventHandler keyEventHandler = new KeyEventHandlerImpl();
    MouseEventHandler mouseEventHandler = new MouseEventHandlerImpl();

    public static ScoreBoard scoreBoard;
    public void runGame(String[] args) {
        launch(args);
    }
    private void addEntity(Entity entity) {
        if(entity instanceof Grass) {
          grasses.add((Grass) entity);
        } else if (entity instanceof StillEntity) {
            stillEntities.add((StillEntity) entity);
        } else if (entity instanceof DynamicEntity) {
            dynamicEntities.add((DynamicEntity) entity);
            if (entity instanceof Bomber) {
                bomber = (Bomber) entity;
            }
        }
    }

    private void loadMap(int level) {
        try {
            File directory = new File(".");
            path = directory.getAbsolutePath() + "/src/main/resources/Map/level_" + level + ".m";
            BufferedReader mapReader = new BufferedReader(new FileReader(new File(path)));
            // read width and height
            String line = mapReader.readLine();
            HEIGHT = 0;
            WIDTH = 0;
            int tmp = 0;
            while(tmp < line.length()) {
                if(line.charAt(tmp) == ' ') {
                    tmp ++;
                    break;
                }
                HEIGHT = HEIGHT * 10 + (line.charAt(tmp ++) - '0');
            }
            while(tmp < line.length()) {
                WIDTH = WIDTH * 10 + (line.charAt(tmp ++) - '0');
            }

            for (int i = 1; i < HEIGHT-1; ++i)
                for (int j = 1; j < WIDTH-1; ++j)
                    addEntity(new Grass(j, i));
            Entity object;
            for (int i = 0; i < HEIGHT; ++i) {
                    line = mapReader.readLine();
                    for (int j = 0; j < WIDTH; ++j) {
                        char type = line.charAt(j);
                        switch (type) {
                            case '#':
                                object = new Wall(j, i);
                                break;
                            case '*':
                                object = new Brick(j, i);
                                break;
                            case 'p':
                                object = new Bomber(j, i);
                                break;
                            case '1':
                                object = new Balloom(j, i);
                                break;
                            case '2':
                                object = new Oneal(j, i);
                                break;
                            case '3':
                                object = new Doll(j, i);
                                break;
                            case '4':
                                object = new Kondoria(j, i);
                                break;
                            default:
                                object = new Grass(j, i);
                                break;
                        }
                        addEntity(object);
                    }
                }
            /// generate items
            ArrayList<Integer> brickIndices = new ArrayList<>();
            for (int i = 0; i < dynamicEntities.size(); ++i)
                if (dynamicEntities.get(i) instanceof Brick)
                    brickIndices.add(i);
            Collections.shuffle(brickIndices);
            ITEMS_PER_LEVEL[level] = (int) CommonFunction.rand(MIN_ITEMS_PER_LEVEL, MAX_ITEMS_PER_LEVEL);
            if (ITEMS_PER_LEVEL[level] > brickIndices.size())
                ITEMS_PER_LEVEL[level] = brickIndices.size();
            for (int i = 0; i < ITEMS_PER_LEVEL[level]; ++i) {
                int index = brickIndices.get(i);
                itemList.add(Item.randomItem((Brick) dynamicEntities.get(index)));
            }
            } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void initEventHandler() {
        keyEventHandler.init(scene);
        mouseEventHandler.init(scene);
    }
    private void loadEventHandler() {
        keyEventHandler.init(scene);
        keyEventHandler.registerEvent(bomber);

        mouseEventHandler.init(scene);
        mouseEventHandler.registerEvent(menu);
        mouseEventHandler.registerEvent(pause);
        mouseEventHandler.registerEvent(gameOver);
    }

    private void clearMap() {
        bombQueue.clear();
        grasses.clear();
        dynamicEntities.clear();
        stillEntities.clear();
        itemList.clear();

        if (bomber != null) keyEventHandler.removeEvent(bomber);

        if (menu != null) mouseEventHandler.removeEvent(menu);
        if (pause != null) mouseEventHandler.removeEvent(pause);
        if (pause != null) mouseEventHandler.removeEvent(gameOver);
    }

    private void checkCollision(Entity entity) {
        boolean bomberFlag = true;
        int cnt = 0;
        for(Entity entity2 : dynamicEntities) {
            if(handleCollision(entity, entity2))
                break;
        }
        for(Entity entity2 : stillEntities) {
            if(handleCollision(entity, entity2))
                break;
        }
        for(Entity entity2 : bombQueue) {
            cnt ++;
            if(handleCollision(entity, entity2)) {
                break;
            }
            if(entity instanceof Bomber && cnt == bombQueue.size()) {
                bomberFlag = false;
            }
        }

        if(!bomberFlag) {
            bomber.onBomb = false;
        }

        if(bombQueue.size() < bomber.getMaxSpawnedBomb()){
            bomber.onBomb = true;
        }
    }

    private boolean handleCollision(Entity entity1, Entity entity2) {
        if(entity1.isColliding(entity2)) {
            entity1.collide(entity2);
            entity2.collide(entity1);
            return true;
        }
        return false;
    }

    private void renderEntities() {
        grasses.forEach(g -> g.render(gc));
        stillEntities.forEach(g -> g.render(gc));
        dynamicEntities.forEach(g -> {
            if (g instanceof Brick)
                g.render(gc);
        });
        for (Item item : itemList)
            item.render(gc);
        for (Bomb bomb : BombermanGame.bombQueue)
            bomb.render(gc);
        bomber.render(gc);
        dynamicEntities.forEach(g -> {
            if (!(g instanceof Brick))
                g.render(gc);
        });
        scoreBoard.render(gc);
    }
    private void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        switch (gameStatus) {
            case MENU:
                menu.render(gc);
                break;
            case RUNNING:
                renderEntities();
                break;
            case PAUSED:
                renderEntities();
                pause.render(gc);
                break;
            case GAME_OVER:
                renderEntities();
                gameOver.render(gc);
                break;
        }
    }

    private void countDown() {
         countDownTask = new TimerTask() {
            @Override
            public void run() {
                if (remainingTime > 0) {
                    --remainingTime;
                }
                else {
                    setGameStatus(GAME_STATUS.GAME_OVER);
                }
            }
        };
        CommonVar.timer.schedule(countDownTask,0, 1000);
    }
    private void reset() {
        gameOverSound.stop();
        score = 0;
        if (countDownTask != null)
            countDownTask.cancel();
        clearMap();
        ObservableList<Node> nodes = root.getChildren();
        for (int i = 0; i < nodes.size(); ++i)
            if (nodes.get(i) instanceof Text)
                nodes.remove(i--);
    }
    private void update() {
        switch (gameStatus) {
            case MENU:
                reset();
                initEventHandler();
                mouseEventHandler.registerEvent(menu);
                if (!screenSound.isPlaying())
                    screenSound.play();
                menu.update();
                break;
            case GAME_LOAD:
                reset();
                scoreBoard = new ScoreBoard(root);
                remainingTime = TIME_PER_LEVEL[level-1];
                loadMap(level);
                loadEventHandler();
                if (!screenSound.isPlaying())
                    screenSound.play();
                countDown();
                setGameStatus(GAME_STATUS.RUNNING);
                break;
            case RUNNING:
                stillEntities.forEach(Entity::update);
                for (int i = 0; i < dynamicEntities.size(); ++i) {
                    DynamicEntity e = dynamicEntities.get(i);
                    if (e.isVanished())
                        dynamicEntities.remove(i--);
                }
                while (!bombQueue.isEmpty() && bombQueue.peek().isVanished())
                    bombQueue.remove();
                for (Bomb bomb : bombQueue)
                    bomb.update();
                for(Entity entity : dynamicEntities) {
                    entity.update();
                    checkCollision(entity);
                }
                scoreBoard.update();
                break;
            case PAUSED:
                pause.update();
                break;
            case GAME_OVER:
                gameOver.update();
                break;
        }
    }

    private static long startTimeStamp = 0;
    private static long currentTimeStamp = 0;
    private static long lastTimeStamp = 0;
    private static long lastSecondStamp = 0;

    /**
     * get current time by nanosecond
     * @return current time by nanosecond
     */
    public static long getTime() {
        return currentTimeStamp - startTimeStamp;
    }

    public static long getFPS() {
        return 1000000000 / (currentTimeStamp - lastTimeStamp);
    }
    public static void setGameStatus(GAME_STATUS gameStatus) {
        if (BombermanGame.gameStatus == gameStatus)
            return;
        BombermanGame.gameStatus = gameStatus;
        if (gameStatus == GAME_STATUS.GAME_OVER) {
            Sound.doAll(Sound::stop);
            gameOverSound.play();
        }
    }
    public static GAME_STATUS getGameStatus() {
        return gameStatus;
    }

    public static GraphicsContext getGc() {
        return gc;
    }

    @Override
    public void start(Stage stage) throws Exception {
        canvas = new Canvas(Sprite.SCALED_SIZE * (WIDTH + ScoreBoard.WIDTH), Sprite.SCALED_SIZE * HEIGHT);
        R_WIDTH = (int) canvas.getWidth();
        R_HEIGHT = (int) canvas.getHeight();
        gc = canvas.getGraphicsContext2D();

        root = new Group();
        root.getChildren().add(canvas);
        scene = new Scene(root);
        stage.setTitle("Bomberman");
        stage.setScene(scene);
//
//        scoreBoard = new ScoreBoard(root);
//        remainingTime = TIME_PER_LEVEL[level-1];
//        loadMap(level);
//        loadEventHandler();
        screenSound = new Sound(Sound.FILE.SCREEN.toString());
        screenSound.setVolume(0.3);
        screenSound.setLoop(-1);
        screenSound.play();

        gameOverSound = new Sound(Sound.FILE.GAME_OVER.toString());
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (startTimeStamp == 0)
                    startTimeStamp = l;
                lastTimeStamp = currentTimeStamp;
                currentTimeStamp = l;
                update();
                render();
            }
        };
        timer.start();
        stage.show();
    }
}
