package hu.mbhw.calendarapp.CalendarGraphics;

import hu.mbhw.calendarapp.data.Event;
import hu.mbhw.calendarapp.data.DataHandler;
import hu.mbhw.calendarapp.renderer.CurrentDayCellRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static hu.mbhw.calendarapp.data.DataHandler.newColumnNames;
import static hu.mbhw.calendarapp.data.DataHandler.newMatrix;

public class MonthView {
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel mainPanel;
    private JComboBox<String> monthComboBox;
    private JList<String> eventList;
    private JTable monthTable;
    private DefaultTableModel monthTableModel;
    private LocalDate localDate;

    public JTable getMonthTable(){
        return monthTable;
    }

    public MonthView(LocalDate localDate) {
        this.localDate=localDate;

        // Left panel setup
        leftPanel();

        // Right panel setup
        rightPanel();

        // Main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return mainPanel;       // Visszaadjuk a fő panelt
    }

    private void leftPanel() {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        // MONTH SELECTOR
        leftPanel = new JPanel(new BorderLayout());
        monthComboBox = new JComboBox<>(months);

        //Set default
        Month monthEnum= localDate.getMonth();
        System.out.print(monthEnum);
        monthComboBox.setSelectedItem(monthEnum.toString().charAt(0) +
                monthEnum.toString().substring(1).toLowerCase());

        // Event list setup (alapértelmezett szűrés nélküli lista)
        eventList = EventList();  // Alapértelmezett értékek

        // Hozzáadjuk a panelhoz mindkét itemet
        leftPanel.add(monthComboBox, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(eventList), BorderLayout.CENTER);
        leftPanel.setPreferredSize(new Dimension(220, 0));
    }

    private void rightPanel() {
        String[] columnNames = newColumnNames();
        String[][] matrix =newMatrix();

        for(Event event: DataHandler.getCurrentlyDisplayedEvents()){
            int i=(event.day- 1) / 7;  // A sor (0-4) meghatározása
            int j=(event.day- 1) % 7;  // A sor (0-4) meghatározása
            matrix[i][j]= matrix[i][j]+ " ("+event.name+")";
        }
        rightPanel = new JPanel(new BorderLayout());
        monthTableModel = new DefaultTableModel(matrix, columnNames);
        monthTable = new JTable(monthTableModel);

        monthTable.setRowHeight(45);
        CurrentDayCellRenderer renderer = new CurrentDayCellRenderer(monthComboBox);
        for (int col = 0; col < monthTable.getColumnCount(); col++) {
            monthTable.getColumnModel().getColumn(col).setCellRenderer(renderer);
        }
        rightPanel.add(new JScrollPane(monthTable), BorderLayout.CENTER);
    }

    public JComboBox<String> getMonthComboBox() {
        return monthComboBox;
    }

    public void updateEventMatrix() {
        String[] columnNames = newColumnNames();
        String[][] matrix=newMatrix();

        for(Event event: DataHandler.getCurrentlyDisplayedEvents()){
            int i=(event.day- 1) / 7;
            int j=(event.day- 1) % 7;
            if(matrix[i][j].length()<=3) {
                matrix[i][j]= matrix[i][j]+ " ("+event.name+")";
            }
        }
        monthTableModel.setDataVector(matrix, columnNames);
        CurrentDayCellRenderer renderer = new CurrentDayCellRenderer(monthComboBox);
        for (int col = 0; col < monthTable.getColumnCount(); col++) {
            monthTable.getColumnModel().getColumn(col).setCellRenderer(renderer);
        }
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    // Event list frissítése a comboboxok értékei alapján
    public void updateEventList() {
        eventList = EventList();

        // Az új lista miatt újra beállítjuk a JScrollPane-t
        leftPanel.removeAll();
        leftPanel.add(monthComboBox, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(eventList), BorderLayout.CENTER);
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    // EventList metódus a szűréshez
    public JList<String> EventList() {
        List<Event> cr = DataHandler.getCurrentlyDisplayedEvents();
        List<String> filteredEvents = new ArrayList<>();
        int num=1;
        for (Event event : cr) {
            filteredEvents.add(num+ ") "+event.name+":  "+event.month+"-"+event.day+"-"+event.hourStart+":00 "+event.place);
            num++;
        }
        return new JList<>(filteredEvents.toArray(new String[0]));
    }
}

