package BombermanGame;

import BombermanGame.Entity.Dynamic.Moving.DIRECTION;
import BombermanGame.Entity.Dynamic.Moving.MOVING_ENTITY_ACTION;
import BombermanGame.TaskHandler.Text;
import BombermanGame.Entity.Dynamic.DynamicEntity;
import BombermanGame.Entity.Dynamic.Moving.Bomber;
import BombermanGame.Entity.Dynamic.Moving.Enemy.*;
import BombermanGame.Entity.Dynamic.NotMoving.Bomb;
import BombermanGame.Entity.Dynamic.NotMoving.Brick;
import BombermanGame.Entity.Entity;
import BombermanGame.Entity.Still.Grass;
import BombermanGame.Entity.Still.Item.Item;
import BombermanGame.Entity.Still.Portal;
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
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.*;

public class BombermanGame extends Application {
    public static int WIDTH = 25;
    public static int HEIGHT = 15;
    public static int R_WIDTH;
    public static int R_HEIGHT;
    public static Queue<Bomb> bombQueue = new LinkedList<>();
    public static int NUMBER_OF_LEVELS = 0;
    private static int level = 2;
    private String path;
    private static ArrayList<DynamicEntity> dynamicEntities = new ArrayList<>();
    private static ArrayList<StillEntity> stillEntities = new ArrayList<>();
    private static ArrayList<Grass> grasses = new ArrayList<>();
    public static Bomber bomber;/// = new Bomber();
    public static Group root;
    private Scene scene;
    private Canvas canvas;
    private GraphicsContext gc;
    private Sound screenSound;
    private TimerTask countDownTask;
    private static Sound gameOverSound;
    private Portal portal = new Portal(WIDTH-2, HEIGHT-2);

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
    private static int[] ITEMS_PER_LEVEL;
    private static final int[] TIME_PER_LEVEL = {600, 600, 600};
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

    static {
        File directory = new File(".");
        NUMBER_OF_LEVELS = 1;
        while (new File(directory.getAbsolutePath() + "/src/main/resources/Map/level_" + NUMBER_OF_LEVELS + ".m").exists())
            ++NUMBER_OF_LEVELS;
        --NUMBER_OF_LEVELS;
        ITEMS_PER_LEVEL = new int[NUMBER_OF_LEVELS];
    }

    public static int getLevel() {
        return level;
    }

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
            ITEMS_PER_LEVEL[level-1] = (int) CommonFunction.rand(MIN_ITEMS_PER_LEVEL, MAX_ITEMS_PER_LEVEL);
            if (ITEMS_PER_LEVEL[level-1] > brickIndices.size())
                ITEMS_PER_LEVEL[level-1] = brickIndices.size();
            for (int i = 0; i < ITEMS_PER_LEVEL[level-1]; ++i) {
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

    private void checkCollision() {
        for(Entity entity1 : dynamicEntities) {
            for(Entity entity2 : dynamicEntities) {
                if(handleCollision(entity1, entity2))
                    break;
            }
            for(Entity entity2 : stillEntities) {
                if(handleCollision(entity1, entity2));
            }
            for(Entity entity2 : bombQueue) {
                if(handleCollision(entity1, entity2))
                    break;
            }
        }

        for(Entity entity1 : stillEntities) {
            for(Entity entity2 : dynamicEntities) {
                if(handleCollision(entity1, entity2))
                    break;
            }
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
        if (completedLevel())
            portal.render(gc);
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
                if (getGameStatus() == GAME_STATUS.PAUSED)
                    return;
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
        if (gameStatus == GAME_STATUS.MENU || bomber == null || bomber.isVanished())
            score = 0;
        gameOverSound.stop();
        if (countDownTask != null)
            countDownTask.cancel();
        clearMap();
        ObservableList<Node> nodes = root.getChildren();
        for (int i = 0; i < nodes.size(); ++i)
            if (nodes.get(i) instanceof Text)
                nodes.remove(i--);
    }
    private boolean completedLevel() {
        return true;
        /**for (DynamicEntity entity : dynamicEntities)
            if (entity instanceof Enemy)
                return false;
        return true;*/
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
                dynamicEntities.forEach(Entity::update);
                checkCollision();
                scoreBoard.update();
                if (bomber.getBoardX() == portal.getBoardX() && bomber.getBoardY() == portal.getBoardY())
                    setGameStatus(GAME_STATUS.TRANSITION_LEVEL);
                break;
            case TRANSITION_LEVEL:
                if (endtrans) {
                    if (level > NUMBER_OF_LEVELS) {
                        level = 1;
                        setGameStatus(GAME_STATUS.MENU);
                    } else {
                        setGameStatus(GAME_STATUS.GAME_LOAD);
                    }
                    endtrans = false;
                }
                else {
                    transitionLevel();
                }
                break;
            case PAUSED:
                pause.update();
                break;
            case GAME_OVER:
                gameOver.update();
                break;
            case END_GAME:
                setGameStatus(GAME_STATUS.MENU);
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

    private boolean running1 = false;
    private boolean running2 = false;
    private boolean running3 = false;
    private boolean endtrans = false;
    private int brunning = 0;
    private void transitionLevel() {
        if (brunning == 0) {
            reset();
            gc.setFill(Color.BLACK);
            brunning = 1;
            bomber = new Bomber(0, HEIGHT / 2);
            bomber.setAction(MOVING_ENTITY_ACTION.MOVING);
            bomber.setDirection(DIRECTION.RIGHT);
            bomber.setSpeed(3);
            return;
        }
        if (brunning == 1) {
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            bomber.update();
            bomber.render(gc);
            if (bomber.getX() > canvas.getWidth())
                brunning = 2;
            return;
        }
        if (running1 || running2 || running3) {
            return;
        }
        endtrans = false;
        ++level;
        System.out.println(level);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        String s;
        int fontSize = 0;
        if (level > NUMBER_OF_LEVELS) {
            screenSound.stop();
            new Sound(Sound.FILE.CONGRATULATION.toString()).play();
            s = "      Congratulation\n You passed all level♥";
            fontSize = 60;
        } else {
            s = "☻ Level " + level;
            fontSize = 100;
        }
        Text content = new Text( s,"MinecraftRegular-Bmg3.otf", fontSize, Color.WHITE);
        content.setX((R_WIDTH - content.getLayoutBounds().getWidth()) / 2);
        content.setY((double) (R_HEIGHT / 2) + content.getLayoutBounds().getHeight() / 3);
        root.getChildren().add(content);
        FadeTransition ft = new FadeTransition();
        ft.setNode(content);

        /// fade in
        ft.setDuration(Duration.seconds(3));
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
        running1 = true;
        ft.setOnFinished(actionEvent -> {
            running1 = false;
            running2 = true;
            FadeTransition ft2 = new FadeTransition();
            ft2.setNode(content);
            ft2.setDuration(Duration.seconds(2));
            ft2.setFromValue(1.0);
            ft2.setToValue(1.0);
            ft2.play();
            ft2.setOnFinished(actionEvent1 -> {
                running2 = false;
                running3 = true;
                FadeTransition ft3 = new FadeTransition();
                ft3.setNode(content);
                ft3.setDuration(Duration.seconds(3));
                ft3.setFromValue(1.0);
                ft3.setToValue(0.0);
                ft3.play();
                ft3.setOnFinished(actionEvent2 -> {
                    running3 = false;
                    endtrans = true;
                    brunning = 0;
                });
            });
        });
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
        ///transitionLevel();
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
                if (gameStatus == GAME_STATUS.TRANSITION_LEVEL)
                    update();
                else {
                    checkCollision();
                    update();
                    render();
                }
            }
        };
        timer.start();
        stage.show();
    }
}
