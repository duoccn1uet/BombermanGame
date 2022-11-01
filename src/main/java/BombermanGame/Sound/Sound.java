package BombermanGame.Sound;

import BombermanGame.BombermanGame;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Sound {
    public static final String RESOURCES_PATH;
    private static ArrayList<Sound> allElements = new ArrayList<>();

    public static enum FILE {
        BOMB_TIMER("bomb timer.mp3"),
        EXPLOSION("explosion.wav"),
        SCREEN("screen.wav"),
        APPLY_ITEM("power_up.wav"),
        BOMBER_DEAD("bomber dead.mp3"),
        GAME_OVER("game over.wav");

        private String name;
        FILE(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    static {
        RESOURCES_PATH = new File(".").getAbsolutePath() + "/src/main/resources/Sound/";
    }

    private Media file;
    private MediaPlayer player;
    public Sound(String fileName) {
        try {
            file = new Media(new File(RESOURCES_PATH + fileName).toURI().toString());
            player = new MediaPlayer(file);
            player.setOnReady(() -> {

            });
            BombermanGame.root.getChildren().add(new MediaView(player));
            allElements.add(this);
        } catch (Exception e) {
            System.out.println("Error occurred when load file " + fileName);
            e.printStackTrace();
        }
    }

    public Duration getDuration() {
        safeToUse();
        return file.getDuration();
    }

    /**
     * play file ${loop} time
     * @param loop times to loop, -1 if infinity
     */
    public void setLoop(int loop) {
        player.setCycleCount(loop);
    }

    public void play() {
        safeToUse();
        player.seek(Duration.ZERO);
        player.play();
    }

    public boolean isPlaying() {
        return player.getStatus() == MediaPlayer.Status.PLAYING;
    }
    
    public void mute() {
        safeToUse();
        player.setMute(true);
    }
    
    public void unMute() {
        safeToUse();
        player.setMute(false);
    }

    public void stop() {
        safeToUse();
        player.stop();
    }

    public void dispose() {
        safeToUse();
        player.dispose();
    }

    public void setVolume(double rate) {
        safeToUse();
        player.setVolume(rate);
    }

    public boolean isDisposed() {
        return player.getStatus() == MediaPlayer.Status.DISPOSED;
    }

    private void safeToUse() {
        boolean safe = true;
        if (isDisposed())
            safe = false;
        if (!safe)
            throw new RuntimeException("File is disposed");
    }
    
    private static void filterUnsafeFile() {
        for (int i = 0; i < allElements.size(); ++i) {
            boolean check = false;
            Sound sound = allElements.get(i);
            if (sound == null || sound.isDisposed())
                check = true;
            if (check)
                allElements.remove(i--);
        }
    }

    public static void doAll(Consumer<? super Sound> action) {
        allElements.forEach(action);
    }
}
