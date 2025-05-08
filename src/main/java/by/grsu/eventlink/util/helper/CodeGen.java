package by.grsu.eventlink.util.helper;

import java.util.Random;

public class CodeGen {

    private final static String PATTERN = "%06d";

    private final static Random RANDOM = new Random();

    public static String generateSixDigits() {
        int number = RANDOM.nextInt(999999);

        return String.format(PATTERN, number);
    }

}
