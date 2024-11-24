package hu.mbhw.calendarapp.actionlisteners;

import hu.mbhw.calendarapp.CalendarGraphics.MonthView;
import hu.mbhw.calendarapp.CalendarGraphics.WeekView;
import hu.mbhw.calendarapp.data.Event;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static hu.mbhw.calendarapp.data.DataHandler.filterEventList;
import static hu.mbhw.calendarapp.data.DataHandler.getCurrentlyDisplayedEvents;

/**
 * Listener for handling ComboBox selections in the Calendar application.
 * This class filters and updates the displayed events in the MonthView and WeekView
 * based on the selected month and event type.
 */
public class ComboBoxActionListener implements ActionListener {

    // ComboBox for selecting the month to filter events.
    private JComboBox<String> monthComboBox;

    // ComboBox for selecting the event type to filter events.
    private JComboBox<String> eventTypeComboBox;

    // Reference to the MonthView, which displays the monthly calendar.
    private MonthView monthView;

    // Reference to the WeekView, which displays the weekly calendar.
    private WeekView weekView;

    /**
     * Constructor to initialize the ComboBoxActionListener with required UI components.
     *
     * @param monthComboBox - ComboBox for selecting the month.
     * @param eventTypeComboBox - ComboBox for selecting the event type.
     * @param monthView - Reference to the MonthView for updating the monthly view.
     * @param weekView - Reference to the WeekView for updating the weekly view.
     */
    public ComboBoxActionListener(JComboBox<String> monthComboBox, JComboBox<String> eventTypeComboBox, MonthView monthView, WeekView weekView) {
        this.monthComboBox = monthComboBox;
        this.eventTypeComboBox = eventTypeComboBox;
        this.monthView = monthView;
        this.weekView = weekView;
    }

    /**
     * Action performed when a selection is made in either ComboBox.
     * Filters the events based on the selected month and event type, then updates the UI.
     *
     * @param e - The ActionEvent triggered by a ComboBox selection.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Retrieve the selected month and event type from the ComboBoxes.
        String selectedMonth = (String) monthComboBox.getSelectedItem();
        String selectedEventType = (String) eventTypeComboBox.getSelectedItem();

        // Filter the events based on the selected criteria (month and type).
        filterEventList(selectedMonth, selectedEventType);

        // Debugging: Print the selected values and the filtered event names to the console.
        System.out.println("------------------\n" + monthComboBox.getSelectedItem() + " " + eventTypeComboBox.getSelectedItem());
        for (Event event : getCurrentlyDisplayedEvents()) {
            System.out.println(event.name);
        }

        // Update the MonthView with the filtered events.
        monthView.updateEventMatrix();  // Updates the visual matrix of the monthly calendar.
        monthView.updateEventList();    // Updates the list of events shown in the MonthView.

        // Update the WeekView with the filtered events.
        weekView.updateWeekTable();     // Refreshes the weekly calendar view.
    }
}
