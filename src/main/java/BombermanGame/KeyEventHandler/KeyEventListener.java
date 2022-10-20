package BombermanGame.KeyEventHandler;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.List;

public interface KeyEventListener {
    List<KeyCode> interestedIn();

    void notify(KeyEvent keyEvent);
}
