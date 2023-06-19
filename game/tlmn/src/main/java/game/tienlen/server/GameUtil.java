package game.tienlen.server;

import java.util.Random;

public class GameUtil {
    private static Random rd = new Random();
    public static int randomBetween(int a, int b) {
        return a + rd.nextInt(b - a);
    }
    public static long getTimeStampInSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    public static int randomMax(int b){
        return randomBetween(0,b);
    }
}
