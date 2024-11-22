package hu.mbhw.calendarapp.main;

import hu.mbhw.calendarapp.CalendarGraphics.CalendarFrame;

import static hu.mbhw.calendarapp.data.DataHandler.*;


public class Main {
    public static void main(String[] args) {
        fileName="./src/main/resources/Data/data.txt";
        loadData();
        CalendarFrame calendarFrame = new CalendarFrame();
        calendarFrame.setVisible(true);
    }
}