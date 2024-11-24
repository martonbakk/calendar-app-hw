package hu.mbhw.calendarapp.CalendarGraphics;

import hu.mbhw.calendarapp.actionlisteners.*;
import hu.mbhw.calendarapp.renderer.InteractiveCellEditor;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * The main frame for the calendar application.
 * This frame provides the user interface for both the MonthView and WeekView,
 * as well as the ability to toggle between these views.
 */
public class CalendarFrame extends JFrame {

    // Panel for the lower section of the frame that displays the current view.
    private JPanel lowPanel;

    // Panel that currently holds either the MonthView or WeekView.
    private JPanel currentLowPanel;

    // Menu bar at the top of the frame.
    private MenuBar menuBar;

    // MonthView to display the monthly calendar.
    private MonthView monthView;

    // WeekView to display the weekly calendar.
    private WeekView weekView;

    // Custom cell editor for interacting with table cells.
    private InteractiveCellEditor interactiveCellEditor;

    // Tracks the current view of the frame (MONTH or WEEK).
    public static Enums.CALENDARFRAME_VIEW currentView = Enums.CALENDARFRAME_VIEW.MONTH;

    /**
     * Constructor for the CalendarFrame.
     * Initializes the frame, sets up the menu bar, views, and listeners.
     *
     * @param localDate - The date to initialize the calendar views.
     */
    public CalendarFrame(LocalDate localDate) {
        // Set up the frame properties.
        setTitle("Calendar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 330);
        setResizable(false);

        // Initialize the menu bar.
        menuBar = new MenuBar();

        // Initialize the MonthView and WeekView.
        monthView = new MonthView(localDate);
        weekView = new WeekView(localDate, monthView.getMonthComboBox());

        // Set up the lower panel to hold the current view.
        lowPanel = new JPanel(new BorderLayout());
        currentLowPanel = monthView.getPanel(); // Start with the MonthView.
        lowPanel.add(currentLowPanel, BorderLayout.CENTER);

        // Initialize the interactive cell editor for table editing.
        interactiveCellEditor = new InteractiveCellEditor(
                monthView, weekView
        );
        setupTableEditors(); // Configure the table editors.

        // Set up listeners for ComboBoxes and buttons.
        comboBoxListener(monthView.getMonthComboBox(), menuBar.getEventTypeComboBox(), monthView);
        addButtonListener();

        // Add the menu bar and lower panel to the frame.
        add(menuBar, BorderLayout.NORTH);
        add(lowPanel, BorderLayout.CENTER);
    }

    /**
     * Sets up the table editors for the MonthView and WeekView tables.
     */
    private void setupTableEditors() {
        JTable monthTable = monthView.getMonthTable();
        JTable weekTable = weekView.getWeekTable();

        // Configure the custom cell editor for the MonthView table.
        monthTable.setDefaultEditor(Object.class, interactiveCellEditor);

        // Configure the custom cell editor for the WeekView table.
        weekTable.setDefaultEditor(Object.class, interactiveCellEditor);
    }

    /**
     * Adds ComboBox listeners to filter events based on month and event type.
     *
     * @param comboBox1 - The ComboBox for selecting the month.
     * @param comboBox2 - The ComboBox for selecting the event type.
     * @param monthView - The MonthView to be updated.
     */
    private void comboBoxListener(JComboBox<String> comboBox1, JComboBox<String> comboBox2, MonthView monthView) {
        // Create and attach a ComboBoxActionListener to both ComboBoxes.
        ComboBoxActionListener comboBoxListener = new ComboBoxActionListener(comboBox1, comboBox2, monthView, weekView);
        comboBox1.addActionListener(comboBoxListener);
        comboBox2.addActionListener(comboBoxListener);
    }

    /**
     * Adds listeners to the buttons in the menu bar.
     * - The View button toggles between MonthView and WeekView.
     * - The Add Event button opens a dialog to add a new event.
     */
    private void addButtonListener() {
        // Attach a listener to toggle the view between MonthView and WeekView.
        menuBar.getViewButton().addActionListener(new ChangeViewButtonListener(this));

        // Attach a listener to open the Add Event dialog.
        menuBar.getAddEventButton().addActionListener(new AddEventButtonListener(menuBar, monthView, weekView));
    }

    /**
     * Toggles the current view between MonthView and WeekView.
     */
    public void toggleView() {
        // Remove the current view panel from the lower panel.
        lowPanel.remove(currentLowPanel);
        System.out.println("SWITCH VIEW: " + currentView.name());

        if (currentView == Enums.CALENDARFRAME_VIEW.MONTH) {
            // Switch to WeekView.
            currentView = Enums.CALENDARFRAME_VIEW.WEEK;
            currentLowPanel = new JPanel();
            weekView.updateWeekTable(); // Update the WeekView data.
            currentLowPanel.add(weekView.getPanel(), BorderLayout.CENTER);
        } else {
            // Switch to MonthView.
            currentView = Enums.CALENDARFRAME_VIEW.MONTH;
            monthView.updateEventList();   // Update the event list.
            monthView.updateEventMatrix(); // Update the event matrix.
            currentLowPanel = new JPanel(new BorderLayout());
            currentLowPanel.add(monthView.getPanel(), BorderLayout.CENTER);
        }

        // Revalidate and repaint the lower panel to reflect the changes.
        lowPanel.add(currentLowPanel, BorderLayout.CENTER);
        lowPanel.revalidate();
        lowPanel.repaint();
    }
}
