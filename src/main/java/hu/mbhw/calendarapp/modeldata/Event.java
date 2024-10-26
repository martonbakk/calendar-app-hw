package hu.mbhw.calendarapp.modeldata;

import java.util.Date;

public class Event {
    public Date startDate;
    public Date endDate;
    public String title;
    public String description;
    public String location;
    public String category;

    public Event(String title, String description, String location, String category, Date startDate, Date endDate) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
