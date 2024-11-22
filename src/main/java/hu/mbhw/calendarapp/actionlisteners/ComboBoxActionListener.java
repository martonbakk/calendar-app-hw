package hu.mbhw.calendarapp.actionlisteners;

import hu.mbhw.calendarapp.CalendarGraphics.MonthView;
import hu.mbhw.calendarapp.CalendarGraphics.WeekView;
import hu.mbhw.calendarapp.data.Event;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static hu.mbhw.calendarapp.data.DataHandler.filterEventList;
import static hu.mbhw.calendarapp.data.DataHandler.getCurrentlyDisplayedEvents;

public class ComboBoxActionListener implements ActionListener {
    private JComboBox<String> monthComboBox;
    private JComboBox<String> eventTypeComboBox;
    private MonthView monthView;
    private WeekView weekView;

    public ComboBoxActionListener(JComboBox<String> monthComboBox, JComboBox<String> eventTypeComboBox, MonthView monthView, WeekView weekView) {
        this.monthComboBox = monthComboBox;
        this.eventTypeComboBox = eventTypeComboBox;
        this.monthView = monthView;
        this.weekView = weekView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedMonth = (String) monthComboBox.getSelectedItem();
        String selectedEventType = (String) eventTypeComboBox.getSelectedItem();

        // Szűrjük az eseményeket és frissítjük a JList-et
        filterEventList(selectedMonth, selectedEventType);
        System.out.println("------------------\n"+monthComboBox.getSelectedItem() + " " + eventTypeComboBox.getSelectedItem());
        for(Event event: getCurrentlyDisplayedEvents()){
            System.out.println(event.name);
        }
        monthView.updateEventMatrix();
        monthView.updateEventList();
        weekView.refreshWeekTable();
    }
}
