package BombermanGame.Menu.Screen;

import BombermanGame.BombermanGame;
import BombermanGame.Entity.Position;
import BombermanGame.GAME_STATUS;
import BombermanGame.Menu.GameButton;
import BombermanGame.Menu.ImageComponent;
import BombermanGame.MouseEventHandler.MouseEventListener;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import java.awt.*;

public class Menu implements MouseEventListener {
    public Image image;
    public Position position;
    public GameButton play;

    public Menu() {
        this.position = new Position(0, 0);
        this.image = ImageComponent.getFxImage("menu.png");
        this.play = new GameButton(300, 350, "play.png", "play_hovered.png");
    }

    public void update() {
        if(play.getBUTTON_STATUS() == 2) {
            BombermanGame.setGameStatus(GAME_STATUS.GAME_LOAD);
            play.setBUTTON_STATUS(0);
        }
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, position.getX(), position.getY());
        play.render(gc);
    }

    @Override
    public void notify(MouseEvent mouseEvent) {
        EventType<? extends Event> eventType = mouseEvent.getEventType();
        if(MouseEvent.MOUSE_MOVED.equals(eventType)) {
            play.hovered(new Position((int)mouseEvent.getX(), (int)mouseEvent.getY()));
        } else if(MouseEvent.MOUSE_CLICKED.equals(eventType) && BombermanGame.getGameStatus() == GAME_STATUS.MENU) {
            if(play.clicked(new Position((int)mouseEvent.getX(), (int)mouseEvent.getY()))) {
                play.setBUTTON_STATUS(2);
            }
        }
    }
}
