package BombermanGame.MouseEventHandler;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class MouseEventHandlerImpl implements MouseEventHandler, EventHandler<MouseEvent> {
    @Override
    public void init(Scene scene) {
        scene.setOnMouseMoved(this);
        scene.setOnMousePressed(this);
        scene.setOnMouseReleased(this);
    }

    @Override
    public void handle(MouseEvent mouseEvent) {

    }
}
