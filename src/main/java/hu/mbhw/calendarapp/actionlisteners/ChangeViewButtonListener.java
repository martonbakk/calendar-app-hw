package hu.mbhw.calendarapp.actionlisteners;

import hu.mbhw.calendarapp.CalendarGraphics.CalendarFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangeViewButtonListener implements ActionListener {
    private CalendarFrame calendarFrame;

    public ChangeViewButtonListener(CalendarFrame calendarFrame) {
        this.calendarFrame = calendarFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        calendarFrame.toggleView(); // Toggle the view between MonthView and WeekView
    }
}
