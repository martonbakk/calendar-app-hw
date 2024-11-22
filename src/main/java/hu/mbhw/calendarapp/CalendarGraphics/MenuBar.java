package hu.mbhw.calendarapp.CalendarGraphics;

import javax.swing.*;
import java.awt.*;

public class MenuBar extends JPanel {
    private JComboBox<String> eventTypeComboBox;
    private JButton viewButton;
    private JButton addEventButton;

    public MenuBar() {
        setBackground(Color.GRAY);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        String[] eventTypes = {"Home", "Work", "Other"};
        JLabel eventTypeLabel = new JLabel("Event Type");


        eventTypeComboBox = new JComboBox<>(eventTypes);
        viewButton = new JButton("Change View");
        addEventButton = new JButton("Add Event");


        Dimension buttonSize =new Dimension(130, 30);
        viewButton.setPreferredSize(buttonSize);
        addEventButton.setPreferredSize(buttonSize);
        add(eventTypeLabel);
        add(eventTypeComboBox);
        add(viewButton);
        add(addEventButton);

    }

    public JButton getViewButton() {
        return viewButton;
    }

    public JButton getAddEventButton() {
        return addEventButton;
    }



    public JComboBox<String> getEventTypeComboBox() {
        return eventTypeComboBox;
    }
}
