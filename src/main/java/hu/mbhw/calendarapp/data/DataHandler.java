package hu.mbhw.calendarapp.data;

import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

/**
 * DataHandler class manages the storage, retrieval, and filtering of event data.
 * It handles reading and writing events to a file, filtering by criteria, and ensuring no scheduling conflicts.
 */
public class DataHandler {

    // List to store all event data loaded from the file.
    private static List<Event> data = new ArrayList<>();

    // List to store the events currently displayed in the UI.
    private static List<Event> currentlyDisplayedEvents = new ArrayList<>();

    // The name of the file where event data is stored.
    public static String fileName;

    /**
     * Loads all events from the file and filters them for display based on the current month.
     *
     * @param localDate - The current date used to determine the default filter.
     */
    public static void loadData(LocalDate localDate) {
        data = readEventsFromFile(); // Load events from the file.
        for (Event event : data) {
            System.out.println(event.name); // Debugging: Print loaded event names.
        }

        // Get the current month and filter events to display "Home" events for this month by default.
        Month monthEnum = localDate.getMonth();
        filterEventList(
                monthEnum.toString().charAt(0) + monthEnum.toString().substring(1).toLowerCase(),
                "Home"
        );
    }

    /**
     * Reads events from the file specified by `fileName`.
     *
     * @return A list of events loaded from the file.
     */
    public static List<Event> readEventsFromFile() {
        List<Event> events = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Event event = createEventFromString(line); // Parse each line into an Event object.
                if (event != null) {
                    events.add(event); // Add valid events to the list.
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle any errors during file reading.
        }
        return events;
    }

    /**
     * Writes the current list of events (`data`) back to the file.
     */
    public static void writeEventsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Event event : data) {
                writer.write(event.toString()); // Convert each event to a string.
                writer.newLine(); // Add a newline after each event.
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle any errors during file writing.
        }
    }

    /**
     * Retrieves the list of events currently being displayed in the UI.
     *
     * @return The list of currently displayed events.
     */
    public static List<Event> currentlyDisplayedEvents() {
        return currentlyDisplayedEvents;
    }

    /**
     * Filters the list of events based on the given month and event type.
     * Updates the `currentlyDisplayedEvents` list with the filtered results.
     *
     * @param month - The month to filter events by (e.g., "January").
     * @param type - The type of events to filter by (e.g., "Home").
     */
    public static void filterEventList(String month, String type) {
        currentlyDisplayedEvents.clear(); // Clear the current display list.
        for (Event event : data) {
            // Check if the event matches the given month and type.
            boolean matches = event.type.equals(type) && event.month.equals(month);
            System.out.println((matches ? "Yes" : "No") + " " + event.name + " "); // Debugging output.
            if (matches) {
                currentlyDisplayedEvents.add(event); // Add matching events to the display list.
            }
        }
    }

    /**
     * Generates a default matrix representing a blank monthly calendar grid.
     *
     * @return A 2D array representing the calendar grid.
     */
    public static String[][] newMatrix() {
        return new String[][] {
                {"1", "2", "3", "4", "5", "6", "7"},
                {"8", "9", "10", "11", "12", "13", "14"},
                {"15", "16", "17", "18", "19", "20", "21"},
                {"22", "23", "24", "25", "26", "27", "28"},
                {"29", "30", "31", "1", "2", "3", "4"}
        };
    }

    /**
     * Generates default column names for the calendar grid (days of the week).
     *
     * @return An array of column names.
     */
    public static String[] newColumnNames() {
        return new String[] {"H", "K", "Sz", "Cs", "P", "Szo", "V"};
    }

    /**
     * Retrieves the list of currently displayed events, sorted by day and time.
     *
     * @return The sorted list of currently displayed events.
     */
    public static List<Event> getCurrentlyDisplayedEvents() {
        sortCurrentlyDisplayedEvents(); // Sort the events before returning.
        return currentlyDisplayedEvents;
    }

    /**
     * Sorts the `currentlyDisplayedEvents` list by day and starting hour.
     */
    private static void sortCurrentlyDisplayedEvents() {
        currentlyDisplayedEvents.sort((e1, e2) -> {
            int dayComparison = Integer.compare(e1.day, e2.day); // Compare by day.
            if (dayComparison != 0) {
                return dayComparison; // Return the day comparison if not equal.
            }
            return Integer.compare(e1.hourStart, e2.hourStart); // Compare by hour if days are equal.
        });
    }

    /**
     * Adds a new event to the database and the currently displayed list, then saves it to the file.
     *
     * @param event - The event to be added.
     */
    public static void insertRecordIntoDatabase(Event event) {
        data.add(event); // Add the event to the main data list.
        currentlyDisplayedEvents.add(event); // Add the event to the display list.
        writeEventsToFile(); // Save the updated data to the file.
    }

    /**
     * Removes an event from both the database and the currently displayed list, then updates the file.
     *
     * @param event - The event to be removed.
     */
    public static void deleteEventFromList(Event event) {
        data.remove(event); // Remove the event from the main data list.
        currentlyDisplayedEvents.remove(event); // Remove the event from the display list.
        writeEventsToFile(); // Save the updated data to the file.
    }

    /**
     * Parses a line of text from the file into an Event object.
     *
     * @param line - A single line of text representing an event.
     * @return The parsed Event object, or null if parsing fails.
     */
    public static Event createEventFromString(String line) {
        String[] parts = line.split(", "); // Split the line into parts.
        if (parts.length == 8) {
            try {
                // Parse event details from the parts.
                String name = parts[0];
                String type = parts[1];
                String month = parts[2];
                int day = Integer.parseInt(parts[3]);
                int hourStart = Integer.parseInt(parts[4]);
                int hourEnd = Integer.parseInt(parts[5]);
                String place = parts[6];
                String details = parts[7].replace(";", ""); // Remove trailing semicolon.
                System.out.println(name); // Debugging: Print the event name.
                return new Event(name, type, month, day, hourStart, hourEnd, place, details);
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Handle errors in parsing numeric fields.
            }
        }
        return null; // Return null if the line is invalid.
    }

    /**
     * Validates whether an event conflicts with existing events.
     *
     * @param eventToCheck - The event to validate.
     * @return True if there are no conflicts, false otherwise.
     */
    public static boolean dateValidationToPreventConflicts(Event eventToCheck) {
        for (Event event : data) {
            // Check if the event conflicts with an existing event (same day, time, and month).
            if (eventToCheck.hourStart == event.hourStart &&
                    eventToCheck.day == event.day &&
                    eventToCheck.month.equals(event.month)) {
                return false; // Conflict found.
            }
        }
        return true; // No conflicts found.
    }
}
