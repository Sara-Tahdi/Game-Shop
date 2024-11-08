package ca.mcgill.ecse321.gamecenter.utilities;

import java.util.Random;

public class TrackingCode {
    private static final char[] chars = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    private static final Random random = new Random();
    public static String nextCode() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            res.append(chars[random.nextInt(0, chars.length)]);
        }
        return res.toString();
    }
}
