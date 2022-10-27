package BombermanGame.MouseEventHandler;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

public class MouseEventHandlerImpl implements MouseEventHandler, EventHandler<MouseEvent> {
    @Override
    public void init(Scene scene) {
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, this);
        scene.addEventFilter(MouseEvent.MOUSE_RELEASED, this);
    }

    @Override
    public void handle(MouseEvent mouseEvent) {

    }
}
