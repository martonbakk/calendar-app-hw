package hu.mbhw.calendarapp.data;

import hu.mbhw.calendarapp.data.StringValidator;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class StringValidatorTest {

    @Test
    void testContainsInvalidCharacters() {
        assertTrue(StringValidator.containsInvalidCharacters("Hello"), "String should not contain invalid characters.");
        assertFalse(StringValidator.containsInvalidCharacters("Invalid,String"), "String contains invalid characters.");
        assertFalse(StringValidator.containsInvalidCharacters("Invalid;String"), "String contains invalid characters.");
    }
}
