package BombermanGame.KeyEventHandler;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyEventHandlerImpl implements KeyEventHandler, EventHandler<KeyEvent> {
    private Map<KeyCode, List<KeyEventListener>> map = new HashMap<>();

    @Override
    public void init(Scene scene) {
        scene.setOnKeyPressed(this);
        scene.setOnKeyReleased(this);
    }

    @Override
    public void registerEvent(KeyEventListener handler) {
        List<KeyCode> list = handler.interestedIn();
        for(KeyCode keyCode : list) {
            if(map.containsKey(keyCode)) {
                map.get(keyCode).add(handler);
            } else {
                List<KeyEventListener> listenerList = new ArrayList<>();
                listenerList.add(handler);
                map.put(keyCode, listenerList);
            }
        }
    }

    @Override
    public void removeEvent(KeyEventListener handler) {
        List<KeyCode> interestedIn = handler.interestedIn();
        for(KeyCode keyCode : interestedIn) {
            List<KeyEventListener> list = map.get(keyCode);
            if(list != null) {
                list.remove(handler);
                if(list.size() == 0)
                    map.remove(keyCode);
            }
        }
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        List<KeyEventListener> list = map.get(keyEvent.getCode());
        if(list != null) {
            for(KeyEventListener keyEventListener : list) {
                keyEventListener.notify(keyEvent);
            }
        }
    }
}
