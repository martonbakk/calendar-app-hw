package hu.mbhw.calendarapp.data;

import java.util.regex.Pattern;

/**
 * The StringValidator class provides utilities for validating strings
 * to ensure they do not contain forbidden characters.
 */
public class StringValidator {

    /**
     * Checks if the given string contains any invalid characters.
     * Invalid characters are defined as ',' (comma) and ';' (semicolon).
     *
     * @param input - The string to validate.
     * @return True if the string does NOT contain invalid characters, false otherwise.
     */
    public static boolean containsInvalidCharacters(String input) {
        // Define a regular expression pattern for forbidden characters.
        String forbiddenChars = "[,;]";

        // Compile the pattern.
        Pattern pattern = Pattern.compile(forbiddenChars);

        // Check if the input string contains any forbidden characters.
        // If no forbidden characters are found, return true (string is valid).
        /*
        * The find() method searches the input string for any sequence of characters that match the regex [;,].
        * If it finds any matches (e.g., the input contains , or ;), it returns true.
        * If no matches are found, it returns false
        */
        return !pattern.matcher(input).find();
    }
}
