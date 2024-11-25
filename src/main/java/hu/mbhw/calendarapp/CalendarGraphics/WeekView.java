package hu.mbhw.calendarapp.CalendarGraphics;

import hu.mbhw.calendarapp.data.Event;
import hu.mbhw.calendarapp.renderer.CurrentDayColumnRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static hu.mbhw.calendarapp.data.DataHandler.getCurrentlyDisplayedEvents;

/**
 * WeekView class represents the weekly view of the calendar.
 * It provides a table-based representation of events for a specific week, along with navigation controls.
 */
public class WeekView {

    // Main panel containing all components of the WeekView.
    private JPanel mainPanel;

    // Buttons for navigating to the previous or next week.
    private JButton leftButton;
    private JButton rightButton;

    // Label displaying the dates for the current week.
    private JLabel weekLabel;

    // The starting day of the current week (Monday).
    private int currentWeekStart;

    // Table for displaying events in the weekly view.
    private JTable weekTable;

    // ScrollPane to contain the weekly table.
    private JScrollPane scrollPaneWeekTable;

    // Model for managing the data in the weekly table.
    private DefaultTableModel weekTableModel;

    // The date used to initialize the WeekView.
    private LocalDate localDate;

    // ComboBox for selecting a month, used for highlighting the current day.
    private JComboBox<String> monthComboBox;

    /**
     * Getter for the weekly table.
     * @return The JTable representing the weekly calendar.
     */
    public JTable getWeekTable() {
        return weekTable;
    }

    /**
     * Constructor to initialize the WeekView with the current date and a ComboBox for month selection.
     *
     * @param localDate - The current date used to initialize the WeekView.
     * @param monthComboBox - ComboBox for selecting a month.
     */
    public WeekView(LocalDate localDate, JComboBox<String> monthComboBox) {
        // Column names for the weekly table.
        String[] columnNames = {"TIME", "H", "K", "Sz", "Cs", "P", "Szo", "V"};

        // Matrix for representing the weekly data (24 rows for hours, 8 columns for time and days).
        String[][] weekMatrix = new String[24][8];

        this.localDate = localDate;
        this.monthComboBox = monthComboBox;

        // Initialize the starting day of the week to Monday.
        currentWeekStart = 1;

        // Initialize the week label to display the current week's days.
        weekLabel = new JLabel();
        updateWeekLabel(); // Set the initial week label text.
        weekLabel.setHorizontalAlignment(SwingConstants.CENTER);
        weekLabel.setVerticalAlignment(SwingConstants.CENTER);

        // Initialize the main panel with a BorderLayout.
        mainPanel = new JPanel(new BorderLayout());

        // Create navigation buttons for changing weeks.
        leftButton = new JButton("Prev");
        rightButton = new JButton("Next");

        setupNavigationButtons(); // Set up action listeners for the navigation buttons.

        // Populate the weekly matrix with data.
        fillWeekMatrix(weekMatrix);

        // Initialize the table model and the table for displaying the weekly view.
        weekTableModel = new DefaultTableModel(weekMatrix, columnNames);
        weekTable = new JTable(weekTableModel);
        weekTable.setRowHeight(40); // Set row height for readability.

        // Add the table to a scroll pane.
        scrollPaneWeekTable = new JScrollPane(weekTable);
        scrollPaneWeekTable.setPreferredSize(new Dimension(450, 220)); // Example dimensions.

        // Add components to the main panel.
        mainPanel.setMaximumSize(new Dimension(100, 200));
        mainPanel.add(weekLabel, BorderLayout.NORTH); // Add the week label at the top.
        mainPanel.add(scrollPaneWeekTable, BorderLayout.CENTER); // Add the table in the center.
        mainPanel.add(leftButton, BorderLayout.WEST); // Add the left navigation button.
        mainPanel.add(rightButton, BorderLayout.EAST); // Add the right navigation button.
    }

    /**
     * Updates the week label to display the dates for the current week.
     */
    private void updateWeekLabel() {
        StringBuilder weekText = new StringBuilder("Heti napok: ");
        int tempDate = currentWeekStart;
        for (int i = tempDate; i < currentWeekStart + 7; i++) {
            if (tempDate <= 30) { // Ensure the date does not exceed 30.
                weekText.append(tempDate).append(" ");
                tempDate++;
            } else {
                tempDate = 1; // Reset to 1 if the date exceeds 30.
            }
        }
        weekLabel.setText(weekText.toString());
    }

    /**
     * Sets up the navigation buttons for changing weeks.
     */
    private void setupNavigationButtons() {
        leftButton.addActionListener(e -> {
            if (currentWeekStart > 7) {
                currentWeekStart -= 7; // Move to the previous week.
                updateWeekLabel(); // Update the week label.
                updateWeekTable(); // Refresh the weekly table.
            }
        });

        rightButton.addActionListener(e -> {
            if (currentWeekStart < 29) {
                currentWeekStart += 7; // Move to the next week.
                updateWeekLabel(); // Update the week label.
                updateWeekTable(); // Refresh the weekly table.
            }
        });
    }

    /**
     * Populates the weekly matrix with time slots and event data.
     *
     * @param weekMatrix - The 2D array representing the weekly calendar.
     */
    private void fillWeekMatrix(String[][] weekMatrix) {
        // Initialize the time slots in the first column of the matrix.
        int clock = 1;
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 8; j++) {
                if (j == 0) { // First column is for time intervals.
                    if (clock < 10) {
                        weekMatrix[i][j] = "0" + (clock - 1) + ":00";
                    } else {
                        weekMatrix[i][j] = (clock - 1) + ":00";
                    }
                    clock++;
                } else {
                    weekMatrix[i][j] = " "; // Empty slot for events.
                }
            }
        }

        // Populate the matrix with event data.
        /*
         * We insert the events to the matrix by the start hour. If the event is longer/shorter than on hour
         * the app cant visualize it.
         */
        List<Event> events = getCurrentlyDisplayedEvents();
        for (Event event : events) {
            int columnIndex = (event.day - currentWeekStart) + 1; // +1 for time column.
            if (columnIndex >= 1 && columnIndex <= 7) { // Ensure the event falls within the current week.
                weekMatrix[event.hourStart][columnIndex] = event.toString();
            }
        }
    }

    /**
     * Updates the weekly table with refreshed data.
     */
    public void updateWeekTable() {
        // Reinitialize the weekly matrix.
        String[][] weekMatrix = new String[24][8];
        fillWeekMatrix(weekMatrix); // Refill the matrix with updated data.

        System.out.println(currentWeekStart + "--- " + localDate.getDayOfMonth() + " ---" + (currentWeekStart + 7));

        // Update the table model with the new matrix.
        weekTableModel.setDataVector(weekMatrix, new String[]{"TIME", "H", "K", "Sz", "Cs", "P", "Szo", "V"});

        // Highlight the current day if it falls within the displayed week.
        Month monthEnum = localDate.getMonth();
        /*
         * We use the localDate to get the current date, then we check if we are in the good week
         * and the month comboBox selected item is equals to the current month
         */
        if (currentWeekStart <= localDate.getDayOfMonth() && localDate.getDayOfMonth() < (currentWeekStart + 7) &&
                monthComboBox.getSelectedItem().toString().contains(monthEnum.toString().charAt(0) +
                        monthEnum.toString().substring(1).toLowerCase())) {
            int currentDayColumn = localDate.getDayOfMonth() % 7;
            for (int i = 1; i <= 7; i++) {
                if (i == currentDayColumn) {
                    //we set the backgroung red
                    weekTable.getColumnModel().getColumn(i).setCellRenderer(new CurrentDayColumnRenderer());
                } else {
                    //Otherwise set white bg
                    weekTable.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer());
                }
            }
        } else {
            for (int i = 1; i <= 7; i++) {
                weekTable.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer());
            }
        }
        weekTable.revalidate();
        weekTable.repaint();
    }

    /**
     * Getter for the main panel containing the WeekView components.
     * @return The main JPanel.
     */
    public JPanel getPanel() {
        return mainPanel;
    }
}
