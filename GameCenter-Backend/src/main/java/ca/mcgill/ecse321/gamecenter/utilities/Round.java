package ca.mcgill.ecse321.gamecenter.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Round {
    public static float round(float aFloat) {
        BigDecimal bg = new BigDecimal(Float.toString(aFloat));
        bg = bg.setScale(2, RoundingMode.UP);
        return bg.floatValue();
    }
}
