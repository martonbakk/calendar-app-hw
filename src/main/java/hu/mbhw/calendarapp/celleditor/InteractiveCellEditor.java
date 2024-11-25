package hu.mbhw.calendarapp.celleditor;

import hu.mbhw.calendarapp.CalendarGraphics.*;
import hu.mbhw.calendarapp.data.DataHandler;
import hu.mbhw.calendarapp.data.Event;
import hu.mbhw.calendarapp.data.StringValidator;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

import static hu.mbhw.calendarapp.CalendarGraphics.CalendarFrame.currentView;

/**
 * The InteractiveCellEditor class provides functionality for interacting with calendar cells in JTable.
 * It allows users to view, edit, and delete events through pop-up dialogs.
 */
public class InteractiveCellEditor extends AbstractCellEditor implements TableCellEditor {

    // Button used to trigger interaction when a cell is clicked.
    private JButton button;

    // The current value of the cell being edited.
    private Object currentValue;

    /**
     * Constructor for InteractiveCellEditor.
     * Sets up the button and its action listener for event interaction.
     *
     * @param monthView - Reference to the MonthView for updating UI after changes.
     * @param weekView - Reference to the WeekView for updating UI after changes.
     */
    public InteractiveCellEditor(MonthView monthView, WeekView weekView) {
        button = new JButton("");

        // Action listener for handling button clicks on calendar cells.
        button.addActionListener(e -> {
            JDialog popup = new JDialog();
            popup.setTitle("Event Details");
            popup.setResizable(false);
            popup.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            popup.setBounds(100, 100, 250, 120);

            // Display brief event details.
            JLabel title = new JLabel("Brief details: " + currentValue.toString());
            popup.add(title);

            // Parse the current event from the cell value.
            Event currentEvent = DataHandler.createEventFromString(currentValue.toString());

            if (currentView == CalendarFrame.CALENDARFRAME_VIEW.WEEK && currentEvent != null) {
                // "Edit" button to modify the event.
                JButton editButton = new JButton("Edit");
                editButton.addActionListener(e1 -> {
                    // Create a pop-up for editing event details.
                    JFrame popup2 = new JFrame("Event Details");
                    popup2.setResizable(false);
                    popup2.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                    popup2.setBounds(100, 100, 250, 300);

                    JPanel detailsPanel = new JPanel();
                    detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS)); // Vertical layout.

                    // Display event details.
                    JLabel eventDetails = new JLabel("Event Details");
                    eventDetails.setFont(new Font("Arial", Font.BOLD, 14));
                    eventDetails.setAlignmentX(Component.CENTER_ALIGNMENT);
                    detailsPanel.add(eventDetails);
                    detailsPanel.add(Box.createVerticalStrut(5)); // Small gap.

                    // Display event type.
                    JLabel eventType = new JLabel("Event type: " + currentEvent.type);
                    eventType.setAlignmentX(Component.CENTER_ALIGNMENT);
                    detailsPanel.add(eventType);

                    // Display event month.
                    JLabel eventMonth = new JLabel("Event month: " + currentEvent.month);
                    eventMonth.setAlignmentX(Component.CENTER_ALIGNMENT);
                    detailsPanel.add(eventMonth);

                    // Display event time.
                    JLabel eventPlace = new JLabel("Event time: " + currentEvent.hourStart + ":00-" + currentEvent.hourEnd + ":00");
                    eventPlace.setAlignmentX(Component.CENTER_ALIGNMENT);
                    detailsPanel.add(eventPlace);

                    // Editable fields for event title and details.
                    JPanel eventTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JLabel eventTitle = new JLabel("Event title:");
                    JTextField eventTitleGiven = new JTextField(currentEvent.name, 20);
                    eventTitlePanel.add(eventTitle);
                    eventTitlePanel.add(eventTitleGiven);
                    detailsPanel.add(eventTitlePanel);

                    JPanel eventPlacePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JLabel eventPlaceLabel = new JLabel("Event take palce:");
                    JTextField eventPlaceGiven = new JTextField(currentEvent.place, 20);
                    eventPlacePanel.add(eventPlaceLabel);
                    eventPlacePanel.add(eventPlaceGiven);
                    detailsPanel.add(eventPlacePanel);

                    JPanel eventDetailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    JLabel eventDetailsLabel = new JLabel("Event details:");
                    JTextField eventDetailsGiven = new JTextField(currentEvent.details, 20);
                    eventDetailsPanel.add(eventDetailsLabel);
                    eventDetailsPanel.add(eventDetailsGiven);
                    detailsPanel.add(eventDetailsPanel);

                    // Save and Cancel buttons for editing.
                    JPanel buttonPanel = new JPanel(new FlowLayout());
                    JButton saveButton = new JButton("Save");
                    saveButton.addActionListener(e2 -> {
                        // Validate input before saving changes.
                        if (StringValidator.containsInvalidCharacters(eventDetailsGiven.getText() + " " + eventTitleGiven.getText()+" "+eventPlaceGiven.getText())) {
                            DataHandler.deleteEventFromList(currentEvent); // Remove the old event.
                            currentEvent.details = eventDetailsGiven.getText();
                            currentEvent.name = eventTitleGiven.getText();
                            currentEvent.place = eventPlaceGiven.getText();
                            DataHandler.insertRecordIntoDatabase(currentEvent); // Save updated event.

                            JOptionPane.showMessageDialog(null, "The event has been successfully edited!",
                                    "Successfully Edited", JOptionPane.INFORMATION_MESSAGE);

                            // Update the views after editing.
                            monthView.updateEventList();
                            monthView.updateEventMatrix();
                            weekView.updateWeekTable();
                            popup2.dispose();
                            popup.dispose();
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "The event has not been edited! Event contains illegal characters.",
                                    "Edit Failed", JOptionPane.ERROR_MESSAGE);
                        }
                    });

                    JButton cancelButton = new JButton("Cancel");
                    cancelButton.addActionListener(e3 -> popup2.dispose());
                    buttonPanel.add(saveButton);
                    buttonPanel.add(cancelButton);

                    popup2.add(detailsPanel);
                    popup2.add(buttonPanel, BorderLayout.SOUTH);
                    popup2.setVisible(true);
                });

                // "Delete" button to remove the event.
                JButton deleteButton = new JButton("Delete");
                deleteButton.addActionListener(e1 -> {
                    Event eventToDelete = DataHandler.createEventFromString(currentValue.toString());
                    if (eventToDelete != null) {
                        DataHandler.deleteEventFromList(eventToDelete);
                        monthView.updateEventMatrix();
                        monthView.updateEventList();
                        weekView.updateWeekTable();

                        JOptionPane.showMessageDialog(null, "The event has been successfully deleted!",
                                "Deletion Successful", JOptionPane.INFORMATION_MESSAGE);
                        popup.dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "The event has not been deleted!",
                                "Deletion Failed", JOptionPane.ERROR_MESSAGE);
                    }
                });

                // Add buttons to the dialog.
                JPanel buttonPanel = new JPanel(new FlowLayout());
                buttonPanel.add(editButton);
                buttonPanel.add(deleteButton);
                popup.add(buttonPanel, BorderLayout.SOUTH);
            }
            popup.setVisible(true);
        });
    }

    /**
     * Retrieves the value of the cell being edited.
     *
     * @return The current value of the cell.
     */
    @Override
    public Object getCellEditorValue() {
        return currentValue;
    }

    /**
     * Prepares the cell editor component for display in the table.
     *
     * @param table - The JTable that is asking the editor to edit.
     * @param value - The value of the cell to be edited.
     * @param isSelected - Whether the cell is selected.
     * @param row - The row index of the cell.
     * @param column - The column index of the cell.
     * @return The component used for editing.
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        // Store the current cell value for use in the dialog.
        currentValue = value.toString();
        return button; // Return the button as the editor component.
    }
}
