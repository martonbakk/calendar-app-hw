package hu.mbhw.calendarapp.app;

import hu.mbhw.calendarapp.CalendarGraphics.CalendarFrame;
import static hu.mbhw.calendarapp.data.DataHandler.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class App {
    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        fileName="./src/main/resources/Data/data.txt";
        loadData();
        CalendarFrame calendarFrame = new CalendarFrame(localDate, localTime);
        calendarFrame.setVisible(true);
    }
}