package cn.balalals.liveboost.util;

public class Utils {
    public static int getRandomNumber(Integer max, Integer min) {
        return min + (int)(Math.random() * (max-min+1));
    }
}
