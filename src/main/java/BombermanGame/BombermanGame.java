package BombermanGame;

import BombermanGame.Entity.Dynamic.DynamicEntity;
import BombermanGame.Entity.Dynamic.Moving.Bomber;
import BombermanGame.Entity.Dynamic.Moving.Enemy.Balloom;
import BombermanGame.Entity.Dynamic.Moving.Enemy.Oneal;
import BombermanGame.Entity.Dynamic.NotMoving.Brick;
import BombermanGame.Entity.Dynamic.NotMoving.NotMovingEntity;
import BombermanGame.Entity.Entity;
import BombermanGame.Entity.Still.Grass;
import BombermanGame.Entity.Still.StillEntity;
import BombermanGame.Entity.Still.Wall;
import BombermanGame.KeyEventHandler.KeyEventHandler;
import BombermanGame.KeyEventHandler.KeyEventHandlerImpl;
import BombermanGame.Menu.ImageComponent;
import BombermanGame.Menu.Screen.GameOver;
import BombermanGame.Menu.Screen.Menu;
import BombermanGame.Menu.Screen.Pause;
import BombermanGame.MouseEventHandler.MouseEventHandler;
import BombermanGame.MouseEventHandler.MouseEventHandlerImpl;
import BombermanGame.Sprite.Sprite;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class BombermanGame extends Application {
    public static int WIDTH = 25;
    public static int HEIGHT = 15;
    private int level = 1;
    private String path;
    private static ArrayList<Entity> dynamicEntities = new ArrayList<>();
    private static ArrayList<Entity> stillEntities = new ArrayList<>();
    private Bomber bomber;/// = new Bomber();
    private Group root;
    private Scene scene;
    private Canvas canvas;
    private GraphicsContext gc;

    public static ArrayList<Entity> getDynamicEntities() {
        return dynamicEntities;
    }

    public static ArrayList<Entity> getStillEntities() {
        return stillEntities;
    }

    // screen
    private Menu menu = new Menu();
    private Pause pause = new Pause();
    private GameOver gameOver = new GameOver();
    public static GAME_STATUS gameStatus = GAME_STATUS.RUNNING;

    // handler
    KeyEventHandler keyEventHandler = new KeyEventHandlerImpl();
    MouseEventHandler mouseEventHandler = new MouseEventHandlerImpl();

    public void runGame(String[] args) {
        launch(args);
    }
    private void addEntity(Entity entity) {
        if (entity instanceof StillEntity) {
            stillEntities.add(entity);
        } else if (entity instanceof DynamicEntity) {
            dynamicEntities.add(entity);
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
            mapReader.readLine();

//            String tmp = mapReader.readLine();
//            HEIGHT = 0;
//            WIDTH = 0;
//            for(int i = 0, flag = 0; i < tmp.length(); i ++) {
//                if(tmp.charAt(i) == ' ')
//                    flag = 1;
//                else if(flag == 0)
//                    HEIGHT = HEIGHT * 10 + (tmp.charAt(i) - '0');
//                else
//                    WIDTH = WIDTH * 10 + (tmp.charAt(i) - '0');
//            }

            for (int i = 1; i < HEIGHT-1; ++i)
                for (int j = 1; j < WIDTH-1; ++j)
                    addEntity(new Grass(j, i));
            for (int i = 0; i < HEIGHT; ++i) {
                ///for (int j = 0; j < WIDTH; ++j) {
                    Entity object;
                    String line = mapReader.readLine();
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
                            default:
                                object = new Grass(j, i);
                                break;
                        }
                        addEntity(object);
                    }
                }
            } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void loadEventHandler() {
        keyEventHandler.init(scene);
        keyEventHandler.registerEvent(bomber);

        mouseEventHandler.init(scene);
        mouseEventHandler.registerEvent(menu);
        mouseEventHandler.registerEvent(pause);
        mouseEventHandler.registerEvent(gameOver);
    }

    private void checkCollision() {
        for(Entity entity1 : dynamicEntities) {
            for(Entity entity2 : dynamicEntities) {
                if(handleCollision(entity1, entity2))
                    break;
            }
            for(Entity entity2 : stillEntities) {
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

    private void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        switch (gameStatus) {
            case MENU:
                menu.render(gc);
                break;
            case RUNNING:
                stillEntities.forEach(g -> g.render(gc));
                dynamicEntities.forEach(g -> g.render(gc));
                bomber.render(gc);
                break;
            case PAUSED:
                stillEntities.forEach(g -> g.render(gc));
                dynamicEntities.forEach(g -> g.render(gc));
                bomber.render(gc);
                pause.render(gc);
                break;
            case GAME_OVER:
                stillEntities.forEach(g -> g.render(gc));
                dynamicEntities.forEach(g -> g.render(gc));
                bomber.render(gc);
                gameOver.render(gc);
                break;
        }
    }

    private void update() {
        switch (gameStatus) {
            case MENU:
                menu.update();
                break;
            case RUNNING:
                stillEntities.forEach(Entity::update);
                for (int i = 0; i < dynamicEntities.size(); ++i) {
                    Entity e = dynamicEntities.get(i);
                    if (e instanceof NotMovingEntity) {
                        if (((NotMovingEntity) e).isVanished())
                            dynamicEntities.remove(i--);
                    }
                }
                dynamicEntities.forEach(Entity::update);
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

    /**
     * get current time by nanosecond
     * @return current time by nanosecond
     */
    public static long getTime() {
        return currentTimeStamp - startTimeStamp;
    }
    public static void setGameStatus(GAME_STATUS gameStatus) {
        BombermanGame.gameStatus = gameStatus;
    }

    @Override
    public void start(Stage stage) throws Exception {
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();

        root = new Group();
        root.getChildren().add(canvas);
        scene = new Scene(root);
        stage.setTitle("Bomberman");
        stage.setScene(scene);

        loadMap(level);
        loadEventHandler();
        for (Entity e : stillEntities)
            if (e instanceof Brick)
                System.out.println(e.getBoardX() + " "  + e.getBoardY());

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (startTimeStamp == 0)
                    startTimeStamp = l;
                currentTimeStamp = l;
                update();
                checkCollision();
                render();
            }
        };
        timer.start();
        stage.show();
    }
}
