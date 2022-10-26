package BombermanGame.Menu.Screen;

import BombermanGame.BombermanGame;
import BombermanGame.Entity.Position;
import BombermanGame.Menu.GameButton;
import BombermanGame.Menu.ImageComponent;
import BombermanGame.MouseEventHandler.MouseEventHandler;
import BombermanGame.MouseEventHandler.MouseEventListener;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;

public class GameOver implements MouseEventListener {
    public Image image;
    public Position position;
    public GameButton retry;
    public GameButton back;

    public GameOver() {
        this.position = new Position(0, 0);
        this.image = ImageComponent.getFxImage("game_over.png");

        this.retry = new GameButton(300, 275, "pause_retry.png", "pause_retry_hovered.png");
        this.back = new GameButton(300, 360, "pause_back.png", "pause_back_hovered.png");
    }

    public void update() {

    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, position.getX(), position.getY());
        retry.render(gc);
        back.render(gc);
    }

    @Override
    public void notify(MouseEvent mouseEvent) {
        EventType<? extends Event> eventType = mouseEvent.getEventType();
        if(MouseEvent.MOUSE_MOVED.equals(eventType)) {
            retry.hovered(new Position((int)mouseEvent.getX(), (int)mouseEvent.getY()));
            back.hovered(new Position((int)mouseEvent.getX(), (int)mouseEvent.getY()));
        } else if(MouseEvent.MOUSE_CLICKED.equals(eventType)) {
            if(retry.clicked(new Position((int)mouseEvent.getX(), (int)mouseEvent.getY()))) {
                // restart level
            } else if(back.clicked(new Position((int)mouseEvent.getX(), (int)mouseEvent.getY()))) {
                // bakc to menu
            }
        }
    }
}
