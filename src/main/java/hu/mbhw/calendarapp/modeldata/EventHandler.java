package hu.mbhw.calendarapp.modeldata;

import java.util.ArrayList;
import java.util.List;

public class EventHandler {
    List<Event> eventList;

    public EventHandler(){
        eventList= new ArrayList<Event>();
    }

    public List<Event> getEvent(){
        return eventList;
    }
}
