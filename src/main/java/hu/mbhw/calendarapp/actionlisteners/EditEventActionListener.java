package hu.mbhw.calendarapp.actionlisteners;

import hu.mbhw.calendarapp.CalendarGraphics.MonthView;
import hu.mbhw.calendarapp.CalendarGraphics.WeekView;
import hu.mbhw.calendarapp.data.DataHandler;
import hu.mbhw.calendarapp.data.Event;
import hu.mbhw.calendarapp.data.StringValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditEventActionListener implements ActionListener {
    private Event eventToEdit;
    private MonthView monthView;
    private WeekView weekView;
    public static boolean savedInput;
    private JFrame popup;

    // Konstruktor: átveszi a módosítandó eseményt
    public EditEventActionListener(Event eventToEdit, MonthView monthView, WeekView weekView, JFrame popup) {
        this.eventToEdit = eventToEdit;
        this.monthView=monthView;
        this.weekView=weekView;
        this.savedInput=false;
        this.popup=popup;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Létrehozzuk az ablakot
        JFrame editFrame = new JFrame("Edit Event");
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFrame.setSize(300, 200);
        editFrame.setLayout(new BorderLayout());

        // Panel a mezőknek
        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2));
        fieldsPanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField(eventToEdit.name);
        fieldsPanel.add(nameField);

        fieldsPanel.add(new JLabel("Details:"));
        JTextField detailsField = new JTextField(eventToEdit.details);
        fieldsPanel.add(detailsField);

        editFrame.add(fieldsPanel, BorderLayout.CENTER);

        // Panel a gomboknak
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        editFrame.add(buttonPanel, BorderLayout.SOUTH);

        // Save gomb eseménykezelő
        saveButton.addActionListener(evt -> {


            // Módosítjuk az esemény adatait


            System.out.print(eventToEdit.name+" "+eventToEdit.details);
            if(StringValidator.containsInvalidCharacters(nameField.getText())&&
                    StringValidator.containsInvalidCharacters(detailsField.getText()))
            {
                DataHandler.deleteEventFromList(eventToEdit);
                eventToEdit.name = nameField.getText();
                eventToEdit.details = detailsField.getText();
                System.out.println("Edit Option Saved");
                DataHandler.insertRecordIntoDatabase(eventToEdit);
                monthView.updateEventList();
                monthView.updateEventMatrix();
                weekView.updateWeekTable();
                // Bezárjuk az ablakot
                JOptionPane.showMessageDialog(editFrame, "Event updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                editFrame.dispose();
                savedInput=true;
                popup.setVisible(false);
            } else {
                savedInput=false;
                JOptionPane.showMessageDialog(editFrame, "Invalid characters detected! Please correct the input.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Cancel gomb eseménykezelő
        cancelButton.addActionListener(evt -> editFrame.dispose());

        // Az ablak megjelenítése
        editFrame.setVisible(true);
    }
}