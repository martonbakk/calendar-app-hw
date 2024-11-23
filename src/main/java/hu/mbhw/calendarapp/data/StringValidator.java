package hu.mbhw.calendarapp.data;

import java.util.regex.Pattern;

public class StringValidator {
    public static boolean containsInvalidCharacters(String input) {
        // Regex a megengedett karakterekre
        String forbiddenChars = "[,;:]";
        Pattern pattern = Pattern.compile(forbiddenChars);
        // Ha NEM talál tiltott karaktereket, igazat ad vissza
        return !pattern.matcher(input).find();
    }
}
