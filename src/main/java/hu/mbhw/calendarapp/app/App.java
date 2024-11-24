package hu.mbhw.calendarapp.app;

import hu.mbhw.calendarapp.CalendarGraphics.CalendarFrame;
import java.time.LocalDate;
import static hu.mbhw.calendarapp.data.DataHandler.*;

public class App {
    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();
        fileName="./src/main/resources/Data/data.txt";
        loadData(localDate);
        CalendarFrame calendarFrame = new CalendarFrame(localDate);
        calendarFrame.setVisible(true);
    }
}