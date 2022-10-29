package BombermanGame.MouseEventHandler;

import javafx.scene.Scene;

public interface MouseEventHandler {
    void init(Scene scene);

    void registerEvent(MouseEventListener mouseEventListener);

    void removeEvent(MouseEventListener mouseEventListener);
}
