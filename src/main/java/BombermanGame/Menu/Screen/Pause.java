package BombermanGame.Menu.Screen;

import BombermanGame.BombermanGame;
import BombermanGame.Entity.Position;
import BombermanGame.GAME_STATUS;
import BombermanGame.Menu.GameButton;
import BombermanGame.Menu.ImageComponent;
import BombermanGame.TaskHandler.MouseEventHandler.MouseEventListener;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class Pause implements MouseEventListener {
    public Image image;
    public Position position;
    public GameButton resume;
    public GameButton back;

    public Pause() {
        this.position = new Position(0, 0);
        this.image = ImageComponent.getFxImage("pause.png");
        this.resume = new GameButton(300, 275, "pause_continue.png", "pause_continue_hovered.png");
        this.back = new GameButton(300, 360, "pause_back.png", "pause_back_hovered.png");
    }

    public void update() {
        if(resume.getBUTTON_STATUS() == 2) {
            BombermanGame.setGameStatus(GAME_STATUS.RUNNING);
            resume.setBUTTON_STATUS(0);
        }
        if (back.getBUTTON_STATUS() == 2) {
            BombermanGame.setGameStatus(GAME_STATUS.MENU);
            back.setBUTTON_STATUS(0);
        }
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, position.getX(), position.getY());
        this.resume.render(gc);
        this.back.render(gc);
    }

    @Override
    public void notify(MouseEvent mouseEvent) {
        EventType<? extends Event> eventType = mouseEvent.getEventType();
        if (MouseEvent.MOUSE_MOVED.equals(eventType)) {
            resume.hovered(new Position((int) mouseEvent.getX(), (int) mouseEvent.getY()));
            back.hovered(new Position((int) mouseEvent.getX(), (int) mouseEvent.getY()));
        } else if (MouseEvent.MOUSE_CLICKED.equals(eventType) && BombermanGame.getGameStatus() == GAME_STATUS.PAUSED) {
            if (resume.clicked(new Position((int) mouseEvent.getX(), (int) mouseEvent.getY())))
                resume.setBUTTON_STATUS(2);
            if (back.clicked(new Position((int) mouseEvent.getX(), (int) mouseEvent.getY())))
                back.setBUTTON_STATUS(2);
        }
    }
}
