package BombermanGame;

import BombermanGame.Entity.Dynamic.DynamicEntity;
import BombermanGame.Entity.Dynamic.Moving.Bomber;
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
    private Bomber bomber = new Bomber();

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
            for (int i = 0; i < HEIGHT; ++i) {
                ///for (int j = 0; j < WIDTH; ++j) {
                    Entity object;
                    String line = mapReader.readLine();
                    for (int j = 0; j < WIDTH; ++j) {
                        char type = line.charAt(j);
                        switch (type) {
                            case '#':
                                object = new Wall(j, i, Sprite.wall.getFxImage());
                                break;
                            case '*':
                                object = new Brick(j, i, Sprite.brick.getFxImage());
                                break;
                            default:
                                object = new Grass(j, i, Sprite.grass.getFxImage());
                                break;
                            /**default:
                                object = new Bomber(j, i, Sprite.player_right.getFxImage());
                                break;*/
                        }
                        addEntity(object);
                    }
                    ///System.out.println(line + " " + line.length());
                    ///System.out.print(type);
                }
                addEntity(bomber = new Bomber(1, 1, Sprite.player_right.getFxImage()));
                ///System.out.println();
            } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillEntities.forEach(g -> g.render(gc));
        dynamicEntities.forEach(g -> g.render(gc));
    }

    private void update() {

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
                render();
                update();
            }
        };
        timer.start();
        stage.show();
    }
}
