package hu.mbhw.calendarapp.renderer;

import hu.mbhw.calendarapp.CalendarGraphics.*;
import hu.mbhw.calendarapp.data.DataHandler;
import hu.mbhw.calendarapp.data.Event;
import hu.mbhw.calendarapp.data.StringValidator;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

import static hu.mbhw.calendarapp.CalendarGraphics.CalendarFrame.currentView;

public class InteractiveCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JButton button;
    private Object currentValue;

    public InteractiveCellEditor(MonthView monthView, WeekView weekView) {
        button= new JButton("");
        button.addActionListener(e -> {
            JDialog popup= new JDialog();
            popup.setTitle("Event Details");
            popup.setResizable(false);
            popup.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            popup.setBounds(100, 100, 250, 120);

            JLabel title=new JLabel("Brief details:"+currentValue.toString());
            popup.add(title);

            Event currentEvent = DataHandler.createEventFromString(currentValue.toString());
            if(currentView==Enums.CALENDARFRAME_VIEW.WEEK&&currentEvent!=null) {
                JButton editButton = new JButton("Edit");
                editButton.addActionListener(e1 -> {
                            JFrame popup2 = new JFrame("Event Details");
                            popup2.setResizable(false);
                            popup2.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                            popup2.setBounds(100, 100, 250, 350);


                            JPanel detailsPanel = new JPanel();
                            detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));  // Függőleges elrendezés

                            // Alap adatokat kiírattatunk
                            JLabel eventDetails = new JLabel("Event Details");
                            eventDetails.setFont(new Font("Arial", Font.BOLD, 14));  // Cím kiemelése
                            eventDetails.setAlignmentX(Component.CENTER_ALIGNMENT);  // Középre igazítás
                            detailsPanel.add(eventDetails);
                            detailsPanel.add(Box.createVerticalStrut(5));  // Kis térköz a címek között

                            JLabel eventType = new JLabel("Event type: " + currentEvent.type);
                            eventType.setAlignmentX(Component.CENTER_ALIGNMENT);  // Középre igazítás
                            detailsPanel.add(eventType);

                            JLabel eventMonth = new JLabel("Event month: " + currentEvent.month);
                            eventMonth.setAlignmentX(Component.CENTER_ALIGNMENT);  // Középre igazítás
                            detailsPanel.add(eventMonth);

                            JLabel eventPlace = new JLabel("Event time: " + currentEvent.hourStart + ":00-" + currentEvent.hourEnd + ":00");
                            eventPlace.setAlignmentX(Component.CENTER_ALIGNMENT);  // Középre igazítás
                            detailsPanel.add(eventPlace);

                            // Térköz a két szekció között
                            detailsPanel.add(Box.createVerticalStrut(10));

                            // Módosítható mezők
                            JLabel changeTitle = new JLabel("Change Details");
                            changeTitle.setFont(new Font("Arial", Font.BOLD, 14));  // Cím kiemelése
                            changeTitle.setAlignmentX(Component.CENTER_ALIGNMENT);  // Középre igazítás
                            detailsPanel.add(changeTitle);
                            detailsPanel.add(Box.createVerticalStrut(5));  // Kis térköz

                            // Cím, amit módosíthatunk
                            JPanel eventTitlePanel = new JPanel();
                            eventTitlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));  // Képzés a címkéknek és szövegmezőknek
                            JLabel eventTitle = new JLabel("Event title:");
                            eventTitle.setPreferredSize(new Dimension(100, 20));  // Cím szélesség beállítása
                            eventTitlePanel.add(eventTitle);

                            JTextField eventTitleGiven = new JTextField(currentEvent.name, 20);  // Szélesség beállítása
                            eventTitleGiven.setPreferredSize(new Dimension(200, 30));  // Magasság is beállítva
                            eventTitlePanel.add(eventTitleGiven);
                            detailsPanel.add(eventTitlePanel);

                            // Esemény részletek módosítása
                            JPanel eventDetailsPanel = new JPanel();
                            eventDetailsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));  // Képzés a címkéknek és szövegmezőknek
                            JLabel eventDetailsLabel = new JLabel("Event details:");
                            eventDetailsLabel.setPreferredSize(new Dimension(100, 20));  // Cím szélesség beállítása
                            eventDetailsPanel.add(eventDetailsLabel);

                            JTextField eventDetailsGiven = new JTextField(currentEvent.details, 20);  // Szélesség beállítása
                            eventDetailsGiven.setPreferredSize(new Dimension(200, 30));  // Magasság is beállítva
                            eventDetailsPanel.add(eventDetailsGiven);
                            detailsPanel.add(eventDetailsPanel);

                            // Térköz a végén
                            detailsPanel.add(Box.createVerticalStrut(10));

                            // Panel beállítások
                            detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Határok hozzáadása a panel körül
                            detailsPanel.setPreferredSize(new Dimension(300, 300));


                            JPanel buttonPanel = new JPanel();
                            buttonPanel.setLayout(new FlowLayout());

                            JButton saveButton = new JButton("Save");
                            saveButton.addActionListener(e2 -> {
                                if(StringValidator.containsInvalidCharacters(eventDetailsGiven.getText()+" "+eventTitleGiven.getText())){
                                    DataHandler.deleteEventFromList(currentEvent);
                                    currentEvent.details=eventDetailsGiven.getText();
                                    currentEvent.name=eventTitleGiven.getText();
                                    DataHandler.insertRecordIntoDatabase(currentEvent);
                                    JOptionPane.showMessageDialog(null,
                                            "The event has been successfully edited!", // Üzenet szövege
                                            "Successfully Edited", // Ablak címe
                                            JOptionPane.INFORMATION_MESSAGE);
                                    monthView.updateEventList();
                                    monthView.updateEventMatrix();
                                    weekView.updateWeekTable();
                                    popup2.dispose();
                                    popup.dispose();
                                }else{
                                    JOptionPane.showMessageDialog(null,
                                            "The event has not been edited! Event contains illegal characters", // Üzenet szövege
                                            "Edit fail", // Ablak címe
                                            JOptionPane.INFORMATION_MESSAGE);
                                }
                            });


                            JButton cancelButton = new JButton("Cancel");
                            cancelButton.addActionListener(e3->{
                                popup2.dispose();
                            });
                            buttonPanel.add(saveButton);
                            buttonPanel.add(cancelButton);


                            popup2.add(detailsPanel);
                            popup2.add(buttonPanel, BorderLayout.SOUTH);
                            popup2.setVisible(true);

                        }
                );
                JButton deleteButton = new JButton("Delete");
                deleteButton.addActionListener(e1 -> {
                    Event eventToDelete = DataHandler.createEventFromString(currentValue.toString());
                    if (eventToDelete != null) {
                        DataHandler.deleteEventFromList(eventToDelete);
                        monthView.updateEventMatrix();
                        monthView.updateEventList();
                        weekView.updateWeekTable();

                        JOptionPane.showMessageDialog(null,
                                "The event has been successfully deleted!", // Üzenet szövege
                                "Deletion Successful", // Ablak címe
                                JOptionPane.INFORMATION_MESSAGE);
                        popup.dispose();
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "The event has not been deleted!", // Üzenet szövege
                                "Deletion failed", // Ablak címe
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                });

            JPanel buttonPanel=new JPanel();
            buttonPanel.setLayout(new FlowLayout());
            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);

            popup.add(buttonPanel, BorderLayout.SOUTH);
            }
            popup.setVisible(true);});
    }

    @Override
    public Object getCellEditorValue() {
        return currentValue;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        // Az aktuális érték tárolása
        currentValue = value.toString();
        return button;
    }
}
