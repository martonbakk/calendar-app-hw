package hu.mbhw.calendarapp.data;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataHandlerTest {

    @BeforeEach
    void setUp() {
        DataHandler.fileName = "test_events.txt";
        DataHandler.insertRecordIntoDatabase(new Event("Meeting", "Work", "November", 24, 10, 11, "Office", "Discuss project"));
        DataHandler.insertRecordIntoDatabase(new Event("Gym", "Home", "November", 25, 18, 19, "Local Gym", "Workout session"));
    }

    @Test
    void testLoadData() {
        DataHandler.loadData(LocalDate.of(2024, 11, 1));
        assertFalse(DataHandler.getCurrentlyDisplayedEvents().isEmpty(), "Events should be loaded.");
    }

    @Test
    void testFilterEventList() {
        DataHandler.filterEventList("November", "Work");
        List<Event> events = DataHandler.getCurrentlyDisplayedEvents();
        assertEquals(3, events.size());
        assertEquals("Meeting", events.get(0).name);
    }

    @Test
    void testInsertRecordIntoDatabase() {
        Event newEvent = new Event("Party", "Home", "November", 26, 20, 23, "Club", "Birthday party");
        DataHandler.insertRecordIntoDatabase(newEvent);
        assertTrue(DataHandler.getCurrentlyDisplayedEvents().contains(newEvent), "The event should be added to the database.");
    }

    @Test
    void testDeleteEventFromList() {
        Event event = new Event("Meeting", "Work", "November", 24, 10, 11, "Office", "Discuss project");
        DataHandler.deleteEventFromList(event);
        assertFalse(DataHandler.getCurrentlyDisplayedEvents().contains(event), "The event should be removed from the database.");
    }

    @Test
    void testDateValidationToPreventConflicts() {
        Event conflictingEvent = new Event("Another Meeting", "Work", "November", 24, 10, 11, "Office", "Conflict test");
        assertFalse(DataHandler.dateValidationToPreventConflicts(conflictingEvent), "There should be a conflict with existing events.");
    }
}
