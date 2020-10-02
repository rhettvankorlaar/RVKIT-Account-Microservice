package nl.rvkit.accountmicroservice.util;

import java.util.Date;

public class Util {
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public static String generateTemporaryPassword() {
        return Util.randomAlphaNumeric(20) + "A1a!";
    }

    public static Date addMinutesToDate(int minutes, Date date) {
        return new Date(date.getTime() + (minutes * 60000));
    }
}
