package BombermanGame.TaskHandler;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;

public class Text extends javafx.scene.text.Text {
    public static String RESOURCES_PATH = new File(".").getAbsolutePath() +"/src/main/resources/Font/";

    public Text(String fontName, int fontSize, Color color) {
        setFont(Font.loadFont("file:" + RESOURCES_PATH + fontName, fontSize));
        setFill(color);
    }

    public Text(String content, String fontName, int fontSize, Color color) {
        this(fontName, fontSize, color);
        setText(content);
    }

    public Text(String content) {
        super(content);
    }

    public Text() {
        super();
    }
}
