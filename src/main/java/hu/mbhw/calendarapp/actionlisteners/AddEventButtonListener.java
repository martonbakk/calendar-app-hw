package hu.mbhw.calendarapp.actionlisteners;

import hu.mbhw.calendarapp.CalendarGraphics.CalendarFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddEventButtonListener implements ActionListener {
    private CalendarFrame calendarFrame;

    public AddEventButtonListener(CalendarFrame calendarFrame) {
        this.calendarFrame = calendarFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Megnyitjuk az "Add Event" ablakot (JDialog)
        openAddEventDialog();
    }

    private void openAddEventDialog() {
        // Létrehozzuk az új JDialog ablakot
        JDialog addEventDialog = new JDialog((Frame) null, "Add New Event", true);
        addEventDialog.setSize(300, 200);
        addEventDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Adjuk hozzá a form elemeket (pl. szövegdobozok, gombok stb.)
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField eventNameField = new JTextField();
        JTextField eventDateField = new JTextField();
        JTextArea eventDescriptionArea = new JTextArea();

        JButton saveButton = new JButton("Save Event");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Itt kellene menteni az új eseményt (pl. adatbázisba vagy listába)
                System.out.println("Event saved: " + eventNameField.getText());  // Itt érdemes majd a mentést végrehajtani
                addEventDialog.dispose();  // Bezárjuk az ablakot
            }
        });

        panel.add(new JLabel("Event Name:"));
        panel.add(eventNameField);
        panel.add(new JLabel("Event Date:"));
        panel.add(eventDateField);
        panel.add(new JLabel("Event Description:"));
        panel.add(new JScrollPane(eventDescriptionArea));
        panel.add(saveButton);

        addEventDialog.add(panel);
        addEventDialog.setVisible(true);  // Megjelenítjük az ablakot
    }
}
