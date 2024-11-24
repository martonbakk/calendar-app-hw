package hu.mbhw.calendarapp.data;

import java.util.Objects;

/**
 * The Event class represents a calendar event.
 * It includes details such as name, type, date, time, location, and additional information.
 * This class also provides methods for string conversion, equality checks, and hash code generation.
 */
public class Event {

    // The name of the event (e.g., "Meeting").
    public String name;

    // The type of the event (e.g., "Home", "Work", "Other").
    public String type;

    // The month in which the event occurs (e.g., "January").
    public String month;

    // The day of the month on which the event occurs.
    public int day;

    // The starting hour of the event (24-hour format).
    public int hourStart;

    // The ending hour of the event (24-hour format).
    public int hourEnd;

    // The location where the event takes place.
    public String place;

    // Additional details or description about the event.
    public String details;

    /**
     * Constructor to initialize an Event object with all necessary details.
     *
     * @param name - The name of the event.
     * @param type - The type of the event (e.g., "Home", "Work").
     * @param month - The month in which the event occurs (e.g., "January").
     * @param day - The day of the month on which the event occurs.
     * @param hourStart - The starting hour of the event (24-hour format).
     * @param hourEnd - The ending hour of the event (24-hour format).
     * @param place - The location where the event takes place.
     * @param details - Additional details about the event.
     */
    public Event(String name, String type, String month, int day, int hourStart, int hourEnd, String place, String details) {
        this.name = name;
        this.type = type;
        this.month = month;
        this.day = day;
        this.hourStart = hourStart;
        this.hourEnd = hourEnd;
        this.place = place;
        this.details = details;
    }

    /**
     * Converts the Event object to a formatted string representation.
     * This format is used for saving the event data to a file.
     *
     * @return A string representation of the event in the format:
     *         "name, type, month, day, hourStart, hourEnd, place, details;"
     */
    @Override
    public String toString() {
        return name + ", " + type + ", " + month + ", " + day + ", " + hourStart + ", " + hourEnd + ", " + place + ", " + details + ";";
    }

    /**
     * Checks if two Event objects are equal based on their fields.
     *
     * @param o - The object to compare with the current Event instance.
     * @return True if all fields of the two Event objects are identical, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Same reference, hence equal.
        if (o == null || getClass() != o.getClass()) return false; // Null or different class.

        // Cast the object to Event for field comparison.
        Event event = (Event) o;

        // Compare all fields for equality.
        return day == event.day &&
                hourStart == event.hourStart &&
                hourEnd == event.hourEnd &&
                name.equals(event.name) &&
                type.equals(event.type) &&
                month.equals(event.month) &&
                place.equals(event.place) &&
                details.equals(event.details);
    }

    /**
     * Generates a hash code for the Event object based on its fields.
     * This method is consistent with the equals method.
     *
     * @return An integer hash code for the Event object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, type, month, day, hourStart, hourEnd, place, details);
    }
}
