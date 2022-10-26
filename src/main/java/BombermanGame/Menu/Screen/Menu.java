package BombermanGame.Menu.Screen;

import BombermanGame.Entity.Position;
import BombermanGame.Menu.GameButton;
import BombermanGame.Menu.ImageComponent;
import BombermanGame.MouseEventHandler.MouseEventListener;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class Menu implements MouseEventListener {
    public Image image;
    public Position position;
    public GameButton play;

    public Menu() {
        this.position = new Position(0, 0);
        this.image = ImageComponent.getFxImage("menu.png");
        this.play = new GameButton(300, 350, "play.png");
    }

    public void update() {

    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, position.getX(), position.getY());
        play.render(gc);
    }

    @Override
    public void notify(MouseEvent mouseEvent) {
        EventType<? extends Event> eventType = mouseEvent.getEventType();
        if(MouseEvent.MOUSE_MOVED.equals(eventType)) {

        }
    }
}
