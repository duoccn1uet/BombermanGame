package BombermanGame;

import BombermanGame.Entity.Dynamic.DynamicEntity;
import BombermanGame.Entity.Dynamic.Moving.Bomber;
import BombermanGame.Entity.Dynamic.Moving.Enemy.Balloom;
import BombermanGame.Entity.Dynamic.NotMoving.Bomb;
import BombermanGame.Entity.Dynamic.NotMoving.Brick;
import BombermanGame.Entity.Entity;
import BombermanGame.Entity.Still.Grass;
import BombermanGame.Entity.Still.StillEntity;
import BombermanGame.Entity.Still.Wall;
import BombermanGame.Map.Map;
import BombermanGame.Sprite.Sprite;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class BombermanGame extends Application {
    public static final int WIDTH = 31;
    public static final int HEIGHT = 13;
    private int level = 1;
    private String path;
    private ArrayList<Entity> dynamicEntities = new ArrayList<>();
    private ArrayList<Entity> stillEntities = new ArrayList<>();
    private Bomber bomber;/// = new Bomber();
    private Canvas canvas;
    private GraphicsContext gc;

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

    private void loadMap() {
        try {
            File directory = new File(".");
            System.out.println();
            path = directory.getAbsolutePath() + "/src/main/resources/Map/level_" + level + ".m";
            BufferedReader mapReader = new BufferedReader(new FileReader(new File(path)));
            mapReader.readLine();
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
                                object = new Bomber(j, i, Sprite.player_right.getFxImage());
                                break;
                            case '1':
                                object = new Balloom(j, i, Sprite.balloom_right1.getFxImage());
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

    private void checkCollision() {
        for(Entity entity1 : dynamicEntities) {
            for(Entity entity2 : dynamicEntities) {
                handleCollision(entity1, entity2);
            }
            for(Entity entity2 : stillEntities) {
                handleCollision(entity1, entity2);
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
        stillEntities.forEach(g -> g.render(gc));
        dynamicEntities.forEach(g -> g.render(gc));
    }

    private void update() {
        stillEntities.forEach(g -> g.update());
        dynamicEntities.forEach(g -> g.update());
    }
    @Override
    public void start(Stage stage) throws Exception {
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();

        Group root = new Group();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);

        loadMap();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();
                checkCollision();
                render();
            }
        };
        timer.start();
        stage.show();
    }
}
