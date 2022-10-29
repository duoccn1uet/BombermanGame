package BombermanGame;

import java.util.Random;

public class CommonFunction {
    private static Random rd = new Random();
    public static long rand(long l, long r) {
        return l + Math.abs(rd.nextLong()) % (r - l + 1);
    }
}
