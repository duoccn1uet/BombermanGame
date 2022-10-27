package BombermanGame.MouseEventHandler;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class MouseEventHandlerImpl implements MouseEventHandler, EventHandler<MouseEvent> {
    private List<MouseEventListener> list = new ArrayList<>();

    @Override
    public void init(Scene scene) {
        scene.setOnMouseMoved(this);
        scene.setOnMouseClicked(this);
//        scene.setOnMousePressed(this);
//        scene.setOnMouseReleased(this);
    }

    @Override
    public void registerEvent(MouseEventListener mouseEventListener) {
        list.add(mouseEventListener);
    }

    @Override
    public void removeEvent(MouseEventListener mouseEventListener) {
        list.remove(mouseEventListener);
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        for(MouseEventListener mouseEventListener : list) {
            mouseEventListener.notify(mouseEvent);
        }
    }
}
