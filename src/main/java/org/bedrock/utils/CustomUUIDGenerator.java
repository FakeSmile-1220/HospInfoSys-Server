package org.bedrock.utils;

import java.util.Random;

public class CustomUUIDGenerator {
    private static final char[] charSet = {
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'A', 'B',
            'C', 'D', 'E', 'F',
            'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z'
    };

    public static String generateUUID(int len) {
        char[] chars = new char[len];

        Random rand = new Random();

        for (int i = 0; i < len; i++) {
            chars[i] = charSet[rand.nextInt(36)];
        }

        return new String(chars);
    }
}
