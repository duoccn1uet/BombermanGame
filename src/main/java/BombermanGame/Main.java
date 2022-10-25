package BombermanGame;

import BombermanGame.Sprite.Sprite;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        BombermanGame game = new BombermanGame();
        game.runGame(args);
    }
}
