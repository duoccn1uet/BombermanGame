package BombermanGame.Menu.Screen;

import BombermanGame.BombermanGame;
import BombermanGame.GAME_STATUS;
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
        if(retry.getBUTTON_STATUS() == 2) {
            BombermanGame.setGameStatus(GAME_STATUS.GAME_LOAD);
            retry.setBUTTON_STATUS(0);
        }
        if(back.getBUTTON_STATUS() == 2) {
            BombermanGame.setGameStatus(GAME_STATUS.MENU);
            back.setBUTTON_STATUS(0);
        }
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
            Position tmp = new Position((int)mouseEvent.getX(), (int)mouseEvent.getY());
            retry.hovered(tmp);
            back.hovered(tmp);
        } else if(MouseEvent.MOUSE_CLICKED.equals(eventType) && BombermanGame.getGameStatus() == GAME_STATUS.GAME_OVER) {
            if(retry.clicked(new Position((int)mouseEvent.getX(), (int)mouseEvent.getY()))) {
                retry.setBUTTON_STATUS(2);
            } else if(back.clicked(new Position((int)mouseEvent.getX(), (int)mouseEvent.getY()))) {
                back.setBUTTON_STATUS(2);
            }
        }
    }
}
