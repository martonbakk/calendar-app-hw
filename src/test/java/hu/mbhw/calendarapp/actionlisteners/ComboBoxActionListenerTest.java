package hu.mbhw.calendarapp.actionlisteners;


import hu.mbhw.calendarapp.CalendarGraphics.MonthView;
import hu.mbhw.calendarapp.CalendarGraphics.WeekView;
import hu.mbhw.calendarapp.actionlisteners.ComboBoxActionListener;
import hu.mbhw.calendarapp.data.DataHandler;
import hu.mbhw.calendarapp.data.Event;
import org.junit.jupiter.api.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ComboBoxActionListenerTest {
    private JComboBox<String> monthComboBox;
    private JComboBox<String> eventTypeComboBox;
    private MonthView monthView;
    private WeekView weekView;
    private ComboBoxActionListener listener;

    @BeforeEach
    void setUp() {
        monthComboBox = new JComboBox<>(new String[]{"January", "February", "November"});
        eventTypeComboBox = new JComboBox<>(new String[]{"Home", "Work", "Other"});
        monthView = new MonthView(LocalDate.now());
        weekView = new WeekView(LocalDate.now(), monthComboBox);

        listener = new ComboBoxActionListener(monthComboBox, eventTypeComboBox, monthView, weekView);

        // Add sample data
        DataHandler.fileName = "test_events.txt";
        DataHandler.insertRecordIntoDatabase(new Event("Meeting", "Work", "November", 24, 10, 11, "Office", "Discuss project"));
        DataHandler.insertRecordIntoDatabase(new Event("Party", "Home", "November", 25, 20, 22, "Club", "Birthday party"));
    }

    @Test
    void testActionPerformedUpdatesDisplayedEvents() {
        // Select specific values
        monthComboBox.setSelectedItem("November");
        eventTypeComboBox.setSelectedItem("Work");

        ActionEvent event = new ActionEvent(monthComboBox, ActionEvent.ACTION_PERFORMED, "ComboBoxChanged");
        listener.actionPerformed(event);

        assertEquals(1, DataHandler.getCurrentlyDisplayedEvents().size(), "Only one event should match the filters.");
        assertEquals("Meeting", DataHandler.getCurrentlyDisplayedEvents().get(0).name, "The event should be 'Meeting'.");
    }
}
