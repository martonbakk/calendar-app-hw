package hu.mbhw.calendarapp.CalendarGraphics;


import hu.mbhw.calendarapp.CalendarGraphics.MenuBar;
import org.junit.jupiter.api.*;
import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class MenuBarTest {

    private MenuBar menuBar;

    @BeforeEach
    void setUp() {
        menuBar = new MenuBar();
    }

    @Test
    void testEventTypeComboBox() {
        JComboBox<String> comboBox = menuBar.getEventTypeComboBox();
        assertNotNull(comboBox, "ComboBox should not be null.");
        assertEquals(3, comboBox.getItemCount(), "ComboBox should have three items.");
    }

    @Test
    void testViewButton() {
        JButton button = menuBar.getViewButton();
        assertNotNull(button, "View button should not be null.");
        assertEquals("Change View", button.getText(), "Button text should be 'Change View'.");
    }

    @Test
    void testAddEventButton() {
        JButton button = menuBar.getAddEventButton();
        assertNotNull(button, "Add Event button should not be null.");
        assertEquals("Add Event", button.getText(), "Button text should be 'Add Event'.");
    }
}