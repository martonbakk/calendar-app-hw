package hu.mbhw.calendarapp.actionlisteners;

import hu.mbhw.calendarapp.CalendarGraphics.CalendarFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listener to handle the action of changing the view in the CalendarFrame.
 * This toggles between the MonthView and WeekView.
 */
public class ChangeViewButtonListener implements ActionListener {

    // Reference to the CalendarFrame instance to toggle the view.
    private CalendarFrame calendarFrame;

    /**
     * Constructor to initialize the listener with the CalendarFrame instance.
     * @param calendarFrame - The CalendarFrame whose view will be toggled.
     */
    public ChangeViewButtonListener(CalendarFrame calendarFrame) {
        this.calendarFrame = calendarFrame;
    }

    /**
     * Action performed when the Change View button is clicked.
     * This method toggles the current view in the CalendarFrame.
     * @param e - The ActionEvent triggered by the button click.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Toggles the view in the CalendarFrame.
        // If the current view is MonthView, it switches to WeekView, and vice versa.
        calendarFrame.toggleView();
    }
}
