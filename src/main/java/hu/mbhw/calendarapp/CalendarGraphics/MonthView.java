package hu.mbhw.calendarapp.CalendarGraphics;

import hu.mbhw.calendarapp.data.Event;
import hu.mbhw.calendarapp.data.DataHandler;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
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


    JTable monthTable;

    public MonthView() {
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
        monthTable = new JTable(matrix, columnNames);
        monthTable.setRowHeight(45);
        monthTable.setDefaultEditor(Object.class, new InteractiveCellEditor());
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
        monthTable=new JTable(matrix, columnNames);
        monthTable.setRowHeight(45);
        monthTable.setDefaultEditor(Object.class, new InteractiveCellEditor());
        rightPanel.removeAll();
        rightPanel.add(new JScrollPane(monthTable), BorderLayout.CENTER);
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
            filteredEvents.add(num+ ") "+event.name+":  "+event.month+"-"+event.day+"-"+event.hour+":00");
            num++;
        }
        return new JList<>(filteredEvents.toArray(new String[0]));
    }
}

// Egyedi cellaeditor, ami kattintásra egy új ablakot jelenít meg
class InteractiveCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JButton button;
    private Object currentValue;

    public InteractiveCellEditor() {
        button = new JButton("X");
        button.addActionListener(e -> {
            // Új ablak megjelenítése kattintáskor
            JFrame popup = new JFrame("Cell Details");
            popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            popup.setSize(200, 100);
            popup.add(new JLabel("Value: " + currentValue), BorderLayout.CENTER);
            popup.setVisible(true);
        });
    }

    @Override
    public Object getCellEditorValue() {
        return currentValue;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentValue = value; // Az aktuális cella értékének mentése
        return button; // Gombként jelenítjük meg a cellát
    }
}

