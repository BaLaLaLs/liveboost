package cn.balalals.liveboost.util;

import java.util.Random;

public class RandomUtil {
    public static int randomInt(int max,int min) {
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }
}
