package hu.mbhw.calendarapp.CalendarGraphics;

import javax.swing.*;
import java.awt.*;

/**
 * MenuBar class represents the top menu panel in the calendar application.
 * It includes controls for selecting event types, toggling views, and adding new events.
 */
public class MenuBar extends JPanel {

    // Dropdown (ComboBox) for selecting the type of event (e.g., Home, Work, Other).
    private JComboBox<String> eventTypeComboBox;

    // Button for toggling between MonthView and WeekView.
    private JButton viewButton;

    // Button for opening the dialog to add a new event.
    private JButton addEventButton;

    /**
     * Constructor to initialize the MenuBar with all its components.
     * The MenuBar includes a ComboBox for event types and two buttons for toggling views and adding events.
     */
    public MenuBar() {
        // Set the background color of the MenuBar.
        setBackground(Color.GRAY);

        // Use FlowLayout to center-align the components within the panel.
        setLayout(new FlowLayout(FlowLayout.CENTER));

        // Define the available event types for the ComboBox.
        String[] eventTypes = {"Home", "Work", "Other"};

        // Create a label for the event type selection.
        JLabel eventTypeLabel = new JLabel("Event Type");

        // Initialize the ComboBox with the event types.
        eventTypeComboBox = new JComboBox<>(eventTypes);

        // Initialize the buttons.
        viewButton = new JButton("Change View");  // Button for toggling views.
        addEventButton = new JButton("Add Event"); // Button for adding a new event.

        // Set preferred size for the buttons for consistent appearance.
        Dimension buttonSize = new Dimension(130, 30);
        viewButton.setPreferredSize(buttonSize);
        addEventButton.setPreferredSize(buttonSize);

        // Add components to the MenuBar in the desired order.
        add(eventTypeLabel);         // Add the label for event type.
        add(eventTypeComboBox);      // Add the ComboBox for event type selection.
        add(viewButton);             // Add the button for changing views.
        add(addEventButton);         // Add the button for adding events.
    }

    /**
     * Getter for the viewButton.
     * @return The button used to toggle between MonthView and WeekView.
     */
    public JButton getViewButton() {
        return viewButton;
    }

    /**
     * Getter for the addEventButton.
     * @return The button used to open the Add Event dialog.
     */
    public JButton getAddEventButton() {
        return addEventButton;
    }

    /**
     * Getter for the eventTypeComboBox.
     * @return The ComboBox used for selecting the event type.
     */
    public JComboBox<String> getEventTypeComboBox() {
        return eventTypeComboBox;
    }
}
