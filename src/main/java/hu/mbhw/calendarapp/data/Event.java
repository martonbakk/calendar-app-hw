package hu.mbhw.calendarapp.data;

import java.util.Objects;

public class Event {
    public String name;
    public String type;
    public String month;
    public int day;
    public int hourStart;
    public int hourEnd;
    public String place;
    public String details;

    // Konstruktor
    public Event(String name, String type, String month, int day, int hourStart, int hourEnd, String place, String details) {
        this.name = name;
        this.type = type;
        this.month = month;
        this.day = day;
        this.hourStart = hourStart;
        this.hourEnd=hourEnd;
        this.place=place;
        this.details = details;
    }

    // Visszaírás a fájlba a megfelelő formátumban
    @Override
    public String toString() {
        return name + ", " + type + ", " + month + ", " + day + ", " + hourStart + ", "+hourEnd+ ", "+place +", " + details + ";";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return day == event.day && hourStart == event.hourStart && hourEnd == event.hourEnd &&
                name.equals(event.name) && type.equals(event.type) && month.equals(event.month) &&
                place.equals(event.place) && details.equals(event.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, month, day, hourStart, hourEnd, place, details);
    }
}

