package hu.mbhw.calendarapp.renderer;

import hu.mbhw.calendarapp.CalendarGraphics.CalendarFrame;
import hu.mbhw.calendarapp.CalendarGraphics.Enums;
import hu.mbhw.calendarapp.CalendarGraphics.MonthView;
import hu.mbhw.calendarapp.CalendarGraphics.WeekView;
import hu.mbhw.calendarapp.actionlisteners.DeleteEventActionListener;
import hu.mbhw.calendarapp.actionlisteners.EditEventActionListener;
import hu.mbhw.calendarapp.data.DataHandler;
import hu.mbhw.calendarapp.data.Event;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class InteractiveCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JButton button;
    private Object currentValue;


    public InteractiveCellEditor(MonthView monthView, WeekView weekView) {
        button = new JButton("");
        JButton deleteButton = new JButton("Delete Event");
        JButton editButton = new JButton("Edit Event");

        button.addActionListener(e -> {
            // Új ablak megjelenítése kattintáskor
            JFrame popup = new JFrame("Event Details");
            popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


            if(CalendarFrame.currentView== Enums.CALENDARFRAME_VIEW.WEEK){
                popup.setSize(250, 200);

                Event currentEvent= DataHandler.createEventFromString(currentValue.toString());

                // Az ablak elrendezésének beállítása
                popup.setLayout(new BorderLayout());

                if(currentEvent!=null) {
                    // A leírás középre helyezése
                    JPanel detailsPanel = new JPanel();
                    detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
                    JLabel titleLabel = new JLabel("Title: " + currentEvent.name);
                    JLabel placeLabel = new JLabel("Place: " + currentEvent.place);
                    JLabel dateLabel = new JLabel("Date: " + currentEvent.hourStart + ":00-" + currentEvent.hourEnd + ":00");
                    JLabel detailsLabel = new JLabel("Details: " + currentEvent.details);


                    detailsPanel.add(titleLabel);
                    detailsPanel.add(placeLabel);
                    detailsPanel.add(dateLabel);
                    detailsPanel.add(detailsLabel);
                    popup.add(detailsPanel, BorderLayout.CENTER);


                    // Hozzáadjuk a gombokat és a leírást
                    JPanel buttonPanel = new JPanel();
                    buttonPanel.setLayout(new FlowLayout());

                    // DeleteEventActionListener hozzáadása
                    deleteButton.addActionListener(new DeleteEventActionListener(
                            currentEvent, // Az aktuális esemény részletei
                            monthView,         // Havi nézet táblázata
                            weekView,           // Heti nézet táblázata
                            popup
                    ));

                    editButton.addActionListener(new EditEventActionListener(
                            currentEvent,
                            monthView,
                            weekView,
                            popup
                    ));
                    buttonPanel.add(deleteButton);
                    buttonPanel.add(editButton);
                    popup.add(buttonPanel, BorderLayout.SOUTH);
                }

            }else{
                popup.setSize(230, 150);
                popup.add(new JLabel("Display:  "+currentValue), BorderLayout.CENTER);
            }
            popup.setResizable(false);
            popup.setVisible(true);
        });
    }

    @Override
    public Object getCellEditorValue() {
        return currentValue.toString();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentValue = value.toString();
        return button;
    }
}