package hu.mbhw.calendarapp.data;

public class Event {
    public String name;
    public String type;
    public String month;
    public int day;
    public int hour;
    public String details;

    // Konstruktor
    public Event(String name, String type, String month, int day, int hour, String details) {
        this.name = name;
        this.type = type;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.details = details;
    }

    // Visszaírás a fájlba a megfelelő formátumban
    @Override
    public String toString() {
        return name + ", " + type + ", " + month + ", " + day + ", " + hour + ", " + details + ";";
    }
}

