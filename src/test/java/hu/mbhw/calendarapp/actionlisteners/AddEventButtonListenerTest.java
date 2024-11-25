package hu.mbhw.calendarapp.actionlisteners;

import hu.mbhw.calendarapp.CalendarGraphics.MenuBar;
import hu.mbhw.calendarapp.CalendarGraphics.MonthView;
import hu.mbhw.calendarapp.CalendarGraphics.WeekView;
import hu.mbhw.calendarapp.data.DataHandler;
import hu.mbhw.calendarapp.data.Event;
import org.junit.jupiter.api.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddEventButtonListenerTest {
    private MenuBar menuBar;
    private MonthView monthView;
    private WeekView weekView;
    private AddEventButtonListener listener;

    @BeforeEach
    void setUp() {
        DataHandler.fileName = "test_events.txt";
        menuBar = new MenuBar();
        monthView = new MonthView(LocalDate.now());
        weekView = new WeekView(LocalDate.now(), monthView.getMonthComboBox());
        listener = new AddEventButtonListener(menuBar, monthView, weekView);
    }

    @Test
    void testOpenAddEventDialog() {
        JButton addEventButton = menuBar.getAddEventButton();
        ActionEvent event = new ActionEvent(addEventButton, ActionEvent.ACTION_PERFORMED, "AddEvent");
        assertDoesNotThrow(() -> listener.actionPerformed(event), "Add Event dialog should open without exceptions.");
    }

    @Test
    void testSaveEventAction() {
        Event newEvent = new Event("Meeting", "Work", "November", 25, 10, 11, "Office", "Discuss project");
        DataHandler.insertRecordIntoDatabase(newEvent);
        assertTrue(DataHandler.getCurrentlyDisplayedEvents().contains(newEvent), "The new event should be saved in the database.");
    }
}