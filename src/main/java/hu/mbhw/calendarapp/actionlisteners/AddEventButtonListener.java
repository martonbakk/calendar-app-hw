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

public class AddEventButtonListener implements ActionListener {
    private final JComboBox<String> eventTypeComboBox;
    private final JComboBox<String> monthComboBox;
    private MonthView monthView;
    private WeekView weekView;

    public AddEventButtonListener(MenuBar menuBar, MonthView monthView, WeekView weekView) {
        this.eventTypeComboBox = menuBar.getEventTypeComboBox();
        this.monthComboBox = monthView.getMonthComboBox();
        this.monthView=monthView;
        this.weekView=weekView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Megnyitjuk az "Add Event" ablakot (JDialog)
        openAddEventDialog();
    }

    private void openAddEventDialog() {
        JDialog addEventDialog = new JDialog((Frame) null, "Add New Event", true);
        addEventDialog.setSize(350, 400);
        addEventDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        addEventDialog.setResizable(false);

        // Létrehozunk egy panelt a form elemek számára
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Selected Options:"));
        panel.add(new JLabel("Month: " + monthComboBox.getSelectedItem().toString()));
        panel.add(new JLabel("Type: " + eventTypeComboBox.getSelectedItem().toString()));
        panel.add(new JLabel("Add new Details:"));

        // Form mezők
        JTextField eventNameField = createTextFieldWithLabel(panel, "Event Name:");
        JTextField eventDayField = createTextFieldWithLabel(panel, "Event Day:");
        JTextField eventStartingHourField = createTextFieldWithLabel(panel, "Starting Hour:");
        JTextField eventEndingHourField = createTextFieldWithLabel(panel, "Ending Hour:");
        JTextField eventPlaceField = createTextFieldWithLabel(panel, "Event Place:");
        JTextArea eventDescriptionArea = createTextAreaWithLabel(panel, "Event Description:");


        // Save gomb hozzáadása
        JButton saveButton = new JButton("Save Event");
        panel.add(saveButton, BorderLayout.CENTER);

        saveButton.addActionListener(saveEventAction(panel, eventNameField, eventDayField, eventStartingHourField,
                eventEndingHourField, eventDescriptionArea, eventPlaceField, addEventDialog));

        addEventDialog.add(new JScrollPane(panel));
        addEventDialog.setVisible(true); // Megjelenítjük az ablakot
    }

    private JTextField createTextFieldWithLabel(JPanel panel, String labelText) {
        panel.add(new JLabel(labelText));
        JTextField textField = new JTextField(20);
        panel.add(textField);
        return textField;
    }

    private JTextArea createTextAreaWithLabel(JPanel panel, String labelText) {
        panel.add(new JLabel(labelText));
        JTextArea textArea = new JTextArea(4, 20);
        panel.add(new JScrollPane(textArea));
        return textArea;
    }

    private ActionListener saveEventAction(JPanel panel, JTextField eventNameField, JTextField eventDayField,
                                           JTextField eventStartingHourField, JTextField eventEndingHourField,
                                           JTextArea eventDescriptionArea, JTextField eventPlaceField, JDialog addEventDialog) {
        return e -> {
            try {
                // Validáljuk az inputot
                validateInput(panel, eventNameField, eventDayField, eventStartingHourField, eventEndingHourField,
                        eventDescriptionArea, eventPlaceField);

                // Adatok összegyűjtése
                String eventName = eventNameField.getText();
                int eventDay = Integer.parseInt(eventDayField.getText());
                int startingHour = Integer.parseInt(eventStartingHourField.getText());
                int endingHour = Integer.parseInt(eventEndingHourField.getText());
                String eventDescription = eventDescriptionArea.getText();
                String eventPlace = eventPlaceField.getText();

                String eventType = (String) eventTypeComboBox.getSelectedItem();
                String month = (String) monthComboBox.getSelectedItem();

                // Event létrehozása
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

                // Mentés adatbázisba
                if(DataHandler.dateValidationToPreventConflicts(event)){
                    // Sikeres visszajelzés
                    JOptionPane.showMessageDialog(panel, "Event successfully saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    DataHandler.insertRecordIntoDatabase(event);
                }else{
                    showErrorDialog(panel, "There are already an event at this date");
                }


                weekView.refreshWeekTable();
                monthView.updateEventMatrix();
                monthView.updateEventList();
                addEventDialog.setVisible(false);


            } catch (NumberFormatException ex) {
                showErrorDialog(panel, "Please enter valid numeric values for day and hours.");
            } catch (IllegalArgumentException ex) {
                showErrorDialog(panel, ex.getMessage());
            }
        };
    }

    private void validateInput(JPanel panel, JTextField eventNameField, JTextField eventDayField,
                               JTextField eventStartingHourField, JTextField eventEndingHourField,
                               JTextArea eventDescriptionArea, JTextField eventPlaceField) {
        // Ellenőrizzük az érvénytelen karaktereket
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

        // Ellenőrizzük a mezők helyességét
        int eventDay = Integer.parseInt(eventDayField.getText());
        int startingHour = Integer.parseInt(eventStartingHourField.getText());
        int endingHour = Integer.parseInt(eventEndingHourField.getText());

        if (eventDay < 1 || eventDay > 31) {
            throw new IllegalArgumentException("The day must be between 1 and 31.");
        }
        if (startingHour < 0 || startingHour > 23 || endingHour < 0 || endingHour > 23 || startingHour >= endingHour) {
            throw new IllegalArgumentException("Invalid start or end hour.");
        }
        if(startingHour>endingHour){
            throw new IllegalArgumentException("Invalid start or end hour. Endhour must be bigger than StartHour");
        }
    }

    private void showErrorDialog(JPanel panel, String errorMessage) {
        JOptionPane.showMessageDialog(panel, errorMessage, "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
