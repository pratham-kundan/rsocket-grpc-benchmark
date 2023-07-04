package org.spr.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static List<String> generateRandomString(int length, int listLength) {
        List<String> messages = new ArrayList<>();

        for (int j = 0; j < listLength; j++) {
            Random random = new Random();
            StringBuilder sb = new StringBuilder(length);

            for (int i = 0; i < length; i++) {
                int randomIndex = random.nextInt(CHARACTERS.length());
                char randomChar = CHARACTERS.charAt(randomIndex);
                sb.append(randomChar);
            }

            messages.add(sb.toString());
        }

        return messages;
    }
}
