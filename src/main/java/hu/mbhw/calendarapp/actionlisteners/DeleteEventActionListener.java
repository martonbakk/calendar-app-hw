package hu.mbhw.calendarapp.actionlisteners;

import hu.mbhw.calendarapp.CalendarGraphics.MonthView;
import hu.mbhw.calendarapp.CalendarGraphics.WeekView;
import hu.mbhw.calendarapp.data.DataHandler;
import hu.mbhw.calendarapp.data.Event;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteEventActionListener implements ActionListener {
    private  Event currentEvent;
    private  MonthView monthView;
    private  WeekView weekView;
    private JFrame popup;

    public DeleteEventActionListener(Event eventDetails, MonthView monthViewTable, WeekView weekViewTable, JFrame popup) {
        this.currentEvent = eventDetails; // Az esemény részletei, például a toString() által generált érték
        this.monthView = monthViewTable; // A havi nézet táblázata
        this.weekView = weekViewTable;  // A heti nézet táblázata
        this.popup = popup;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(currentEvent!=null) {
            System.out.print("Delete Operation");
            DataHandler.deleteEventFromList(currentEvent);
            monthView.updateEventList();
            monthView.updateEventMatrix();
            weekView.refreshWeekTable();
            // Visszajelzés a felhasználónak
            JOptionPane.showMessageDialog(null, "Event deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            popup.setVisible(false);
        }
    }
}
