package hu.mbhw.calendarapp.actionlisteners;

import hu.mbhw.calendarapp.CalendarGraphics.MenuBar;
import hu.mbhw.calendarapp.CalendarGraphics.MonthView;
import hu.mbhw.calendarapp.CalendarGraphics.WeekView;
import hu.mbhw.calendarapp.data.DataHandler;
import hu.mbhw.calendarapp.data.Event;
import hu.mbhw.calendarapp.data.StringValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Listener to handle the action of adding a new event via a dialog box.
 */
public class AddEventButtonListener implements ActionListener {
    // Dropdown for selecting event type (e.g., Home, Work).
    private final JComboBox<String> eventTypeComboBox;
    // Dropdown for selecting the month.
    private final JComboBox<String> monthComboBox;
    private MonthView monthView; // Reference to the MonthView.
    private WeekView weekView;   // Reference to the WeekView.

    /**
     * Constructor to initialize dependencies.
     * @param menuBar - Reference to the menu bar for accessing UI components.
     * @param monthView - Reference to the MonthView UI.
     * @param weekView - Reference to the WeekView UI.
     */
    public AddEventButtonListener(MenuBar menuBar, MonthView monthView, WeekView weekView) {
        this.eventTypeComboBox = menuBar.getEventTypeComboBox();
        this.monthComboBox = monthView.getMonthComboBox();
        this.monthView = monthView;
        this.weekView = weekView;
    }

    /**
     * Action performed when the Add Event button is clicked.
     * Opens a dialog to add a new event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Open the "Add Event" dialog.
        openAddEventDialog();
    }

    /**
     * Creates and opens a dialog for adding a new event.
     */
    private void openAddEventDialog() {
        JDialog addEventDialog = new JDialog((Frame) null, "Add New Event", true);
        addEventDialog.setSize(350, 400);
        addEventDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        addEventDialog.setResizable(false);

        // Panel to contain form elements for the event details.
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Selected Options:"));
        panel.add(new JLabel("Month: " + monthComboBox.getSelectedItem().toString()));
        panel.add(new JLabel("Type: " + eventTypeComboBox.getSelectedItem().toString()));
        panel.add(new JLabel("Add new Details:"));

        // Form fields for the event details.
        JTextField eventNameField = createTextFieldWithLabel(panel, "Event Name:");
        JTextField eventDayField = createTextFieldWithLabel(panel, "Event Day:");
        JTextField eventStartingHourField = createTextFieldWithLabel(panel, "Starting Hour:");
        JTextField eventEndingHourField = createTextFieldWithLabel(panel, "Ending Hour:");
        JTextField eventPlaceField = createTextFieldWithLabel(panel, "Event Place:");
        JTextArea eventDescriptionArea = createTextAreaWithLabel(panel, "Event Description:");

        // Save button to confirm and save the event details.
        JButton saveButton = new JButton("Save Event");
        panel.add(saveButton, BorderLayout.CENTER);

        // Attach a listener to the save button for handling event saving.
        saveButton.addActionListener(saveEventAction(panel, eventNameField, eventDayField, eventStartingHourField,
                eventEndingHourField, eventDescriptionArea, eventPlaceField, addEventDialog));

        addEventDialog.add(new JScrollPane(panel));
        addEventDialog.setVisible(true); // Display the dialog.
    }

    /**
     * Helper method to create a text field with a label.
     * @param panel - The panel to which the field is added.
     * @param labelText - The label text.
     * @return The created JTextField.
     */
    private JTextField createTextFieldWithLabel(JPanel panel, String labelText) {
        panel.add(new JLabel(labelText));
        JTextField textField = new JTextField(20);
        panel.add(textField);
        return textField;
    }

    /**
     * Helper method to create a text area with a label.
     * @param panel - The panel to which the area is added.
     * @param labelText - The label text.
     * @return The created JTextArea.
     */
    private JTextArea createTextAreaWithLabel(JPanel panel, String labelText) {
        panel.add(new JLabel(labelText));
        JTextArea textArea = new JTextArea(4, 20);
        panel.add(new JScrollPane(textArea));
        return textArea;
    }

    /**
     * Creates an ActionListener to handle saving the event.
     * @param panel - The parent panel for showing validation messages.
     * @param eventNameField - Field for the event name.
     * @param eventDayField - Field for the event day.
     * @param eventStartingHourField - Field for the starting hour.
     * @param eventEndingHourField - Field for the ending hour.
     * @param eventDescriptionArea - Text area for the event description.
     * @param eventPlaceField - Field for the event place.
     * @param addEventDialog - The dialog to close on successful save.
     * @return An ActionListener for the Save button.
     */
    private ActionListener saveEventAction(JPanel panel, JTextField eventNameField, JTextField eventDayField,
                                           JTextField eventStartingHourField, JTextField eventEndingHourField,
                                           JTextArea eventDescriptionArea, JTextField eventPlaceField, JDialog addEventDialog) {
        return e -> {
            try {
                // Validate the input fields.
                validateInput(panel, eventNameField, eventDayField, eventStartingHourField, eventEndingHourField,
                        eventDescriptionArea, eventPlaceField);

                // Gather event details from input fields.
                String eventName = eventNameField.getText();
                int eventDay = Integer.parseInt(eventDayField.getText());
                int startingHour = Integer.parseInt(eventStartingHourField.getText());
                int endingHour = Integer.parseInt(eventEndingHourField.getText());
                String eventDescription = eventDescriptionArea.getText();
                String eventPlace = eventPlaceField.getText();

                String eventType = (String) eventTypeComboBox.getSelectedItem();
                String month = (String) monthComboBox.getSelectedItem();

                // Create a new Event object.
                Event event = new Event(
                        eventName,
                        eventType,
                        month,
                        eventDay,
                        startingHour,
                        endingHour,
                        eventPlace,
                        eventDescription
                );

                // Save the event to the database if there are no conflicts.
                if (DataHandler.dateValidationToPreventConflicts(event)) {
                    JOptionPane.showMessageDialog(panel, "Event successfully saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    DataHandler.insertRecordIntoDatabase(event);
                } else {
                    showErrorDialog(panel, "There is already an event at this date.");
                }

                // Update the MonthView and WeekView to reflect the changes.
                weekView.updateWeekTable();
                monthView.updateEventMatrix();
                monthView.updateEventList();
                addEventDialog.setVisible(false); // Close the dialog.

            } catch (NumberFormatException ex) {
                // Handle invalid numeric input.
                showErrorDialog(panel, "Please enter valid numeric values for day and hours.");
            } catch (IllegalArgumentException ex) {
                // Handle invalid input validation.
                showErrorDialog(panel, ex.getMessage());
            }
        };
    }

    /**
     * Validates the input fields to ensure all values are correct.
     * @param panel - The parent panel for displaying error messages.
     * @param eventNameField - Field for the event name.
     * @param eventDayField - Field for the event day.
     * @param eventStartingHourField - Field for the starting hour.
     * @param eventEndingHourField - Field for the ending hour.
     * @param eventDescriptionArea - Text area for the event description.
     * @param eventPlaceField - Field for the event place.
     */
    private void validateInput(JPanel panel, JTextField eventNameField, JTextField eventDayField,
                               JTextField eventStartingHourField, JTextField eventEndingHourField,
                               JTextArea eventDescriptionArea, JTextField eventPlaceField) {
        // Validate for invalid characters.
        if (!StringValidator.containsInvalidCharacters(
                eventNameField.getText() +
                        eventDayField.getText() +
                        eventStartingHourField.getText() +
                        eventEndingHourField.getText() +
                        eventDescriptionArea.getText() +
                        eventPlaceField.getText()
        )) {
            throw new IllegalArgumentException("Invalid characters found in input.");
        }

        // Validate day, start, and end hours.
        int eventDay = Integer.parseInt(eventDayField.getText());
        int startingHour = Integer.parseInt(eventStartingHourField.getText());
        int endingHour = Integer.parseInt(eventEndingHourField.getText());

        if (eventDay < 1 || eventDay > 31) {
            throw new IllegalArgumentException("The day must be between 1 and 31.");
        }
        if (startingHour < 0 || startingHour > 23 || endingHour < 0 || endingHour > 23 || startingHour >= endingHour) {
            throw new IllegalArgumentException("Invalid start or end hour.");
        }
    }

    /**
     * Displays an error message in a dialog box.
     * @param panel - The parent panel for the dialog.
     * @param errorMessage - The error message to display.
     */
    private void showErrorDialog(JPanel panel, String errorMessage) {
        JOptionPane.showMessageDialog(panel, errorMessage, "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
