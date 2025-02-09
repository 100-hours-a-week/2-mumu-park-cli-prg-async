package util;

import java.util.Random;

public class RandomNumGenerator {
    private static final Random randomGenerator = new Random();

    public static int generate(int range, int correctionValue) {
        return randomGenerator.nextInt(range) + correctionValue;
    }
}
