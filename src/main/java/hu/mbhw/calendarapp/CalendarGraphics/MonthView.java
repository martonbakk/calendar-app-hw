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

/**
 * MonthView class represents the UI for displaying a monthly calendar.
 * It includes a month selector, an event list, and a matrix-based calendar table.
 */
public class MonthView {

    // Panel for the left side of the MonthView (month selector and event list).
    private JPanel leftPanel;

    // Panel for the right side of the MonthView (monthly calendar table).
    private JPanel rightPanel;

    // Main panel containing both the left and right panels.
    private JPanel mainPanel;

    // ComboBox for selecting a month.
    private JComboBox<String> monthComboBox;

    // List displaying the events for the selected month and type.
    private JList<String> eventList;

    // Table for displaying the monthly calendar grid.
    private JTable monthTable;

    // Model for the monthTable.
    private DefaultTableModel monthTableModel;

    // Current date for initializing the view.
    private LocalDate localDate;

    /**
     * Getter for the monthTable.
     * @return The JTable representing the monthly calendar.
     */
    public JTable getMonthTable() {
        return monthTable;
    }

    /**
     * Constructor to initialize the MonthView with the current date.
     * Sets up the left and right panels and combines them into the main panel.
     *
     * @param localDate - The current date used to initialize the MonthView.
     */
    public MonthView(LocalDate localDate) {
        this.localDate = localDate;

        // Setup the left panel (month selector and event list).
        leftPanel();

        // Setup the right panel (calendar grid).
        rightPanel();

        // Combine the left and right panels into the main panel.
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
    }

    /**
     * Getter for the main panel containing the MonthView components.
     * @return The main JPanel.
     */
    public JPanel getPanel() {
        return mainPanel;
    }

    /**
     * Sets up the left panel with a month selector and an event list.
     */
    private void leftPanel() {
        // Define the months for the ComboBox.
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        // Initialize the left panel with a BorderLayout.
        leftPanel = new JPanel(new BorderLayout());

        // Initialize the ComboBox for selecting a month.
        monthComboBox = new JComboBox<>(months);

        // Set the default selected month to the current month.
        Month monthEnum = localDate.getMonth();
        monthComboBox.setSelectedItem(monthEnum.toString().charAt(0) +
                monthEnum.toString().substring(1).toLowerCase());

        // Initialize the event list with default values.
        eventList = EventList();

        // Add the ComboBox and event list to the left panel.
        leftPanel.add(monthComboBox, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(eventList), BorderLayout.CENTER);
        leftPanel.setPreferredSize(new Dimension(220, 0)); // Set the preferred width of the panel.
    }

    /**
     * Sets up the right panel with a table representing the monthly calendar grid.
     */
    private void rightPanel() {
        // Retrieve column names and initialize the calendar matrix.
        String[] columnNames = newColumnNames();
        String[][] matrix = newMatrix();

        // Populate the matrix with event data.
        for (Event event : DataHandler.getCurrentlyDisplayedEvents()) {
            int i = (event.day - 1) / 7;  // Determine the row based on the day.
            int j = (event.day - 1) % 7;  // Determine the column based on the day.
            matrix[i][j] = matrix[i][j] + " (" + event.name + ")";
        }

        // Initialize the right panel with a BorderLayout.
        rightPanel = new JPanel(new BorderLayout());

        // Create a table model and table for the calendar.
        monthTableModel = new DefaultTableModel(matrix, columnNames);
        monthTable = new JTable(monthTableModel);

        // Set row height for better visibility.
        monthTable.setRowHeight(45);

        // Add a custom renderer for highlighting the current day.
        CurrentDayCellRenderer renderer = new CurrentDayCellRenderer(monthComboBox);
        for (int col = 0; col < monthTable.getColumnCount(); col++) {
            monthTable.getColumnModel().getColumn(col).setCellRenderer(renderer);
        }

        // Add the table to the right panel.
        rightPanel.add(new JScrollPane(monthTable), BorderLayout.CENTER);
    }

    /**
     * Getter for the monthComboBox.
     * @return The JComboBox for selecting a month.
     */
    public JComboBox<String> getMonthComboBox() {
        return monthComboBox;
    }

    /**
     * Updates the calendar table matrix with the currently displayed events.
     */
    public void updateEventMatrix() {
        // Retrieve column names and reinitialize the calendar matrix.
        String[] columnNames = newColumnNames();
        String[][] matrix = newMatrix();

        // Populate the matrix with updated event data.
        for (Event event : DataHandler.getCurrentlyDisplayedEvents()) {
            int i = (event.day - 1) / 7;  // Determine the row.
            int j = (event.day - 1) % 7;  // Determine the column.
            if (matrix[i][j].length() <= 3) { // Ensure no duplicate event entries.
                matrix[i][j] = matrix[i][j] + " (" + event.name + ")";
            }
        }

        // Update the table model with the new matrix.
        monthTableModel.setDataVector(matrix, columnNames);

        // Refresh the cell renderer for the updated table.
        CurrentDayCellRenderer renderer = new CurrentDayCellRenderer(monthComboBox);
        for (int col = 0; col < monthTable.getColumnCount(); col++) {
            monthTable.getColumnModel().getColumn(col).setCellRenderer(renderer);
        }

        // Refresh the left panel to reflect changes.
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    /**
     * Updates the event list based on the ComboBox selections.
     */
    public void updateEventList() {
        // Generate a new event list based on current selections.
        eventList = EventList();

        // Replace the event list in the left panel.
        leftPanel.removeAll();
        leftPanel.add(monthComboBox, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(eventList), BorderLayout.CENTER);

        // Refresh the panel to reflect changes.
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    /**
     * Generates a JList of events based on the currently displayed events.
     *
     * @return A JList containing the events.
     */
    public JList<String> EventList() {
        // Retrieve the currently displayed events.
        List<Event> cr = DataHandler.getCurrentlyDisplayedEvents();

        // Create a list to store formatted event strings.
        List<String> filteredEvents = new ArrayList<>();
        int num = 1; // Counter for numbering the events.

        // Format each event and add it to the list.
        for (Event event : cr) {
            filteredEvents.add(num + ") " + event.name + ":  " +
                    event.month + "-" + event.day + "-" +
                    event.hourStart + ":00 " + event.place);
            num++;
        }

        // Convert the list to an array and create a JList from it.
        return new JList<>(filteredEvents.toArray(new String[0]));
    }
}
