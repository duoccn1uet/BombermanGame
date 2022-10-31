package BombermanGame.ScoreBoard;

import BombermanGame.CommonVar;
import BombermanGame.BombermanGame;
import BombermanGame.Entity.Dynamic.NotMoving.Brick;
import BombermanGame.Entity.Entity;
import BombermanGame.Entity.Still.Grass;
import BombermanGame.Entity.Still.Item.Item;
import BombermanGame.Sprite.Sprite;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Content order:
 * 1: Time: thời gian còn lại
 * 2: High score
 * 3: Score
 * 4: Bomb: số lượng bomb tối đa được đặt
 * 5: Enemy: Số lượng enemy đã tiêu diệt (có phân từng loại)
 * 6: Item: thời gian còn tác dụng của từng loại item
 */
public class ScoreBoard {
    public static final int WIDTH = 8;

    private static final class Background {
        private static final Brick border = new Brick(0, 0);
        private static final Grass ground = new Grass(0, 0);

        Background() {

        }

        public static void render(GraphicsContext gc) {
            for (int i = 0; i < BombermanGame.HEIGHT; ++i)
                for (int j = BombermanGame.WIDTH; j < BombermanGame.WIDTH + WIDTH; ++j)
                    if (i == 0 || i == BombermanGame.HEIGHT - 1 || j == BombermanGame.WIDTH || j == BombermanGame.WIDTH + WIDTH - 1) {
                        border.setBoardX(j);
                        border.setBoardY(i);
                        border.render(gc);
                    } else {
                        ground.setBoardX(j);
                        ground.setBoardY(i);
                        ground.render(gc);
                    }
        }
    }

    private interface KeyProperty {
        int getWidth();

        int getHeight();
    }

    private static class ImageKeyProperty implements KeyProperty {
        private Image img;

        public ImageKeyProperty(String imageName) {
            img = Sprite.getImage(imageName);
        }

        public ImageKeyProperty(Image img) {
            this.img = img;
        }

        @Override
        public int getWidth() {
            return (int) img.getWidth();
        }

        @Override
        public int getHeight() {
            return (int) img.getHeight();
        }

        public void update() {

        }
    }

    private static class TextKeyProperty extends Text implements KeyProperty {

        public TextKeyProperty(String content) {
            super(content);
            setFont(Property.FONT);
            setFill(Property.CONTENT_COLOR);
        }

        @Override
        public int getWidth() {
            return (int) getLayoutBounds().getWidth();
        }

        @Override
        public int getHeight() {
            return (int) getLayoutBounds().getHeight();
        }
    }

    private static class EntityKeyProperty implements KeyProperty {

        private Entity key;
        boolean scheduled = false;
        private int remainingTime;

        public EntityKeyProperty(Entity key) {
            this.key = key;
        }

        public EntityKeyProperty(Entity key, int remainingTime) {
            this.key = key;
            this.remainingTime = remainingTime;
        }

        @Override
        public int getWidth() {
            return (int) key.getImg().getWidth();
        }

        @Override
        public int getHeight() {
            return (int) key.getImg().getHeight();
        }
    }

    private enum PROPERTY_KEY {
//        TIME_REMAINING,
//        HIGH_SCORE,
//        SCORE,
//        MAX_BOMB_SPAWN,
//        ENEMY_KILLED,
//        ITEM;
        TIME_REMAINING,
        HIGH_SCORE,
        SCORE,
        MAX_BOMB_SPAWN,
        ENEMY_KILLED,
        ITEM;
        private int value = -1;
        public int getValue() {
            if (value != -1)
                return value;
            for (int i = 0; i < values().length; ++i)
                if (this == values()[i]) {
                    value = i;
                }
            return value;
        }
        public static int size() {
            return values().length;
        }
    }

    private static abstract class Property {
        private int x;

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
            if (this.content instanceof TextKeyProperty)
                this.content.setY(y);
            else
                this.content.setY(y + this.key.getHeight() - 6);
        }

        private int y;
        private KeyProperty key;
        private Text content;
        private static final String FONT_PATH;
        private static final Font FONT;
        private static final double FONT_SIZE;
        private static final Color CONTENT_COLOR;

        static {
            FONT_PATH = new File(".").getAbsolutePath() + "/src/main/resources/Font/minecraft-font/MinecraftRegular-Bmg3.otf";
            FONT_SIZE = 28;
            FONT = Font.loadFont("file:" + FONT_PATH, FONT_SIZE);
            CONTENT_COLOR = Color.WHITE;
        }

        public Property(KeyProperty key, Text content, int x, int y) {
            this.key = key;
            this.content = content;
            this.content.setFont(FONT);
            this.content.setFill(CONTENT_COLOR);
            this.x = x;
            this.y = y;
            if (this.key instanceof TextKeyProperty) {
                ((TextKeyProperty) this.key).setX(x);
                ((TextKeyProperty) this.key).setY(y+this.key.getHeight());
                this.content.setX(x + this.key.getWidth());
                this.content.setY(((TextKeyProperty) this.key).getY());
            } else {
                this.content.setX(x + this.key.getWidth());
                this.content.setY(y + this.key.getHeight() - 6);
            }
        }

        public KeyProperty getKey() {
            return key;
        }

        public Text getContent() {
            return content;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getHeight() {
            return key.getHeight();
        }

        public abstract void update();

        public void render(GraphicsContext gc) {
            if (key instanceof ImageKeyProperty) {
                gc.drawImage(((ImageKeyProperty) key).img, x, y);
            } else if (key instanceof EntityKeyProperty) {
                gc.drawImage(((EntityKeyProperty) key).key.getImg(), x, y);
            }
        }

    }

    private static final int PROPERTY_X = 850;
    private static final int PROPERTY_START_Y = 90;
    private static final int LINE_SPACING = 20;
    private static Text title = new Text();
    private ArrayList<Property>[] properties = new ArrayList[PROPERTY_KEY.size()];
    private Group root;
    private Text fps = new Text();

    public ScoreBoard(Group root) {
        this.root = root;
        for (int i = 0; i < properties.length; ++i)
            properties[i] = new ArrayList<>();
        for (PROPERTY_KEY property : PROPERTY_KEY.values())
            initProperty(property);

        ObservableList<Node> a = root.getChildren();
        title.setText("board");
        title.setFont(Font.loadFont("file:" + Property.FONT_PATH, 40));
        title.setFill(Color.DARKGOLDENROD);
        title.setX(PROPERTY_X + 17);
        title.setY(PROPERTY_START_Y - 25);
        a.add(title);

        fps.setText("FPS: ");
        fps.setFont(Font.loadFont("file:" + Property.FONT_PATH, 15));
        fps.setFill(Color.BLACK);
        fps.setX(BombermanGame.R_WIDTH - 32 - 55);
        fps.setY(BombermanGame.R_HEIGHT - 32 - 7);
        a.add(fps);
        for (PROPERTY_KEY propertyKey : PROPERTY_KEY.values()) {
            for (Property property : properties[propertyKey.getValue()]) {
                if (property.key instanceof TextKeyProperty) {
                    a.add((TextKeyProperty) property.key);
                }
                a.add(property.content);
            }
        }
    }

    private int getPropertyY(PROPERTY_KEY p, int j) {
        int i = p.getValue();
        Property last;
        if (j > 0) {
            last = properties[i].get(j - 1);
        } else {
            if (i == 0)
                return PROPERTY_START_Y;
            int k = i - 1;
            while (k >= 0 && properties[k].isEmpty())
                --k;
            if (k < 0)
                return PROPERTY_START_Y;
            last = properties[k].get(properties[k].size() - 1);
        }
        return last.getY() + last.getHeight() + LINE_SPACING;
    }

    private void initProperty(PROPERTY_KEY property) {
        int i = property.getValue();
        switch (property) {
            case TIME_REMAINING:
                KeyProperty key = new ImageKeyProperty("clock1.png");
                Text content = new Text(" default");
                properties[i].add(new Property(key, content, PROPERTY_X, getPropertyY(property, 0)) {
                    @Override
                    public void update() {
                        content.setText(" " + BombermanGame.getRemainingTime() + "s");
                    }
                });
                break;
            case HIGH_SCORE:
                key = new ImageKeyProperty("high score.png");
                content = new Text(" default");
                properties[i].add(new Property(key, content, PROPERTY_X, getPropertyY(property, 0)) {
                    @Override
                    public void update() {

                    }
                });
                break;
            case SCORE:
                key = new ImageKeyProperty("score.png");
                content = new Text(" default");
                properties[i].add(new Property(key, content, PROPERTY_X, getPropertyY(property, 0)) {
                    @Override
                    public void update() {
                        content.setText(" " + BombermanGame.score);
                    }
                });
                break;
            case MAX_BOMB_SPAWN:
                key = new ImageKeyProperty("bomb - score_board.png");
                content = new Text(" default");
                properties[i].add(new Property(key, content, PROPERTY_X, getPropertyY(property, 0)) {
                    @Override
                    public void update() {
                        content.setText(" x" + BombermanGame.bomber.getMaxSpawnedBomb());
                    }
                });
                break;
            case ENEMY_KILLED:
                break;
            case ITEM:
        }
    }

    public void render(GraphicsContext gc) {
        Background.render(gc);
        for (PROPERTY_KEY propertyKey : PROPERTY_KEY.values()) {
            for (Property property : properties[propertyKey.getValue()])
                property.render(gc);
        }
    }

    public void addItemProperty(Item item) {
        if (item.getApplyDuration() < 0)
            return;
        int i = PROPERTY_KEY.ITEM.getValue();
        int id = properties[i].size();
        for (int j = 0; j < properties[i].size(); ++j) {
            Property itemProperty = properties[i].get(j);
            if (((EntityKeyProperty) itemProperty.key).key.getClass().equals(item.getClass())) {
                id = j;
                root.getChildren().remove(itemProperty.content);
                properties[i].remove(j);
                break;
            }
        }
        EntityKeyProperty key = new EntityKeyProperty(item, (int) (item.getApplyDuration() / (int) 1e9));
        Text content = new Text(" Default");
        root.getChildren().add(content);
        properties[i].add(id, new Property(key, content, PROPERTY_X, getPropertyY(PROPERTY_KEY.ITEM, id)) {
            @Override
            public void update() {
                EntityKeyProperty eKey = (EntityKeyProperty) getKey();
                if (eKey.scheduled)
                    return;
                eKey.scheduled = true;
                CommonVar.timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (eKey.remainingTime >= 0) {
                            content.setText(" " + eKey.remainingTime + "s");
                            --eKey.remainingTime;
                        }
                    }
                }, 0, 1000);
            }
        });

    }

    private boolean renderFPS = false;
    public void update() {
        if (!renderFPS) {
            renderFPS = true;
            CommonVar.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    fps.setText("FPS: " + BombermanGame.getFPS());
                }
            }, 0, 1000);
        }
        for (PROPERTY_KEY propertyKey : PROPERTY_KEY.values()) {
            int i = propertyKey.getValue();
            for (int j = 0; j < properties[i].size(); ++j) {
                Property property = properties[i].get(j);
                if (property.key instanceof EntityKeyProperty) {
                    if (((EntityKeyProperty) property.key).remainingTime < 0) {
                        root.getChildren().remove(property.content);
                        properties[i].remove(j);
                        j--;
                    } else
                        property.update();
                } else
                    property.update();
            }
        }
        for (PROPERTY_KEY propertyKey : PROPERTY_KEY.values()) {
            int i = propertyKey.getValue();
            for (int j = 0; j < properties[i].size(); ++j) {
                Property property = properties[i].get(j);
                property.setY(getPropertyY(propertyKey, j));
            }
        }
    }
}
