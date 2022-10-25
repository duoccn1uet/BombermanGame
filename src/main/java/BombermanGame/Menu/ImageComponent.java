package BombermanGame.Menu;

import javafx.scene.image.*;

import java.io.File;
import java.io.FileInputStream;

public class ImageComponent {
    public static String path;
    public static final int TRANSPARENT_COLOR = 0xffff00ff;

    static {
        path = new File(".").getAbsolutePath() + "/src/main/resources/Menu";
    }

    public static Image getFxImage(String imageName) {
        Image img = getImage(imageName);
        PixelReader r = img.getPixelReader();
        int width = (int) img.getWidth();
        int height = (int) img.getHeight();
        WritableImage wr = new WritableImage(width, height);
        PixelWriter w = wr.getPixelWriter();
        for (int i = 0; i < height; ++i)
            for (int j = 0; j < width; ++j) {
                int argb = r.getArgb(j, i);
                if (argb == TRANSPARENT_COLOR) {
                    w.setArgb(j, i, 0);
                } else {
                    w.setArgb(j, i, argb);
                }
            }
        Image input = new ImageView(wr).getImage();
        return resample(input, 1);
    }

    public static Image getImage(String imageName) {
        try {
            FileInputStream f = new FileInputStream(path + "/" + imageName);
            return new Image(f);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static Image resample(Image input, int scaleFactor) {
        final int W = (int) input.getWidth();
        final int H = (int) input.getHeight();
        final int S = scaleFactor;

        WritableImage output = new WritableImage(
                W * S,
                H * S
        );

        PixelReader reader = input.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                final int argb = reader.getArgb(x, y);
                for (int dy = 0; dy < S; dy++) {
                    for (int dx = 0; dx < S; dx++) {
                        writer.setArgb(x * S + dx, y * S + dy, argb);
                    }
                }
            }
        }

        return output;
    }

}
