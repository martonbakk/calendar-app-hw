package hu.mbhw.calendarapp.CalendarGraphics;

import hu.mbhw.calendarapp.actionlisteners.*;
import hu.mbhw.calendarapp.data.DataHandler;
import hu.mbhw.calendarapp.data.Event;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class CalendarFrame extends JFrame {
    private JPanel lowPanel;
    private JPanel currentLowPanel;

    private MenuBar menuBar;
    private MonthView monthView;
    private WeekView weekView;
    private InteractiveCellEditor interactiveCellEditor;

    public static Enums.CALENDARFRAME_VIEW currentView = Enums.CALENDARFRAME_VIEW.MONTH;

    public CalendarFrame() {
        setTitle("Calendar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 330);
        setResizable(false);

        // MenuBar setup
        menuBar = new MenuBar();

        // Initialize views
        monthView = new MonthView();
        weekView = new WeekView();

        // Lower panel setup
        lowPanel = new JPanel(new BorderLayout());
        currentLowPanel = monthView.getPanel();
        lowPanel.add(currentLowPanel, BorderLayout.CENTER);

        // Table renderer
        interactiveCellEditor = new InteractiveCellEditor(
                monthView, weekView
        );
        setupTableEditors();

        // ComboBox Listener setup
        comboBoxListener(monthView.getMonthComboBox(), menuBar.getEventTypeComboBox(), monthView);
        addButtonListener();

        // Add panels to frame
        add(menuBar, BorderLayout.NORTH);
        add(lowPanel, BorderLayout.CENTER);
    }

    private void setupTableEditors() {
        JTable monthTable = monthView.getMonthTable();
        JTable weekTable = weekView.getWeekTable();

        // MonthView táblázat cellaeditor beállítása
        monthTable.setDefaultEditor(Object.class, interactiveCellEditor);

        // WeekView táblázat cellaeditor beállítása
        weekTable.setDefaultEditor(Object.class, interactiveCellEditor);
    }

    private void comboBoxListener(JComboBox<String> comboBox1, JComboBox<String> comboBox2, MonthView monthView) {
        ComboBoxActionListener comboBoxListener = new ComboBoxActionListener(comboBox1, comboBox2, monthView, weekView);
        comboBox1.addActionListener(comboBoxListener);
        comboBox2.addActionListener(comboBoxListener);
    }

    private void addButtonListener() {
        menuBar.getViewButton().addActionListener(new ChangeViewButtonListener(this));
        menuBar.getAddEventButton().addActionListener(new AddEventButtonListener(this));
    }

    public void toggleView(){
        lowPanel.remove(currentLowPanel);
        System.out.println("SWITCH VIEW: "+ currentView.name());
        if (currentView==Enums.CALENDARFRAME_VIEW.MONTH) {
            currentView = Enums.CALENDARFRAME_VIEW.WEEK;
            // Switch to Week View
            currentLowPanel = new JPanel();
            currentLowPanel.add(weekView.getPanel(), BorderLayout.CENTER);
        } else {
            currentView = Enums.CALENDARFRAME_VIEW.MONTH;
            // Switch to Month View
            currentLowPanel = new JPanel(new BorderLayout());
            currentLowPanel.add(monthView.getPanel(), BorderLayout.CENTER);
        }

        // Revalidate and repaint the low panel to reflect the changes
        lowPanel.add(currentLowPanel, BorderLayout.CENTER);
        lowPanel.revalidate();
        lowPanel.repaint();
    }

}
class InteractiveCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JButton button;
    private Object currentValue;


    public InteractiveCellEditor(MonthView monthView, WeekView weekView) {
        button = new JButton("");
        JButton deleteButton = new JButton("Delete Event");
        JButton editButton = new JButton("Edit Event");

        button.addActionListener(e -> {
            // Új ablak megjelenítése kattintáskor
            JFrame popup = new JFrame("Event Details");
            popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


            if(CalendarFrame.currentView==Enums.CALENDARFRAME_VIEW.WEEK){
                popup.setSize(250, 200);

                Event currentEvent=DataHandler.createEventFromString(currentValue.toString());

                // Az ablak elrendezésének beállítása
                popup.setLayout(new BorderLayout());

                if(currentEvent!=null) {
                    // A leírás középre helyezése
                    JPanel detailsPanel = new JPanel();
                    detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
                    JLabel titleLabel = new JLabel("Title: " + currentEvent.name);
                    JLabel placeLabel = new JLabel("Place: " + currentEvent.place);
                    JLabel dateLabel = new JLabel("Date: " + currentEvent.hourStart + ":00-" + currentEvent.hourEnd + ":00");
                    JLabel detailsLabel = new JLabel("Details: " + currentEvent.details);


                    detailsPanel.add(titleLabel);
                    detailsPanel.add(placeLabel);
                    detailsPanel.add(dateLabel);
                    detailsPanel.add(detailsLabel);
                    popup.add(detailsPanel, BorderLayout.CENTER);


                    // Hozzáadjuk a gombokat és a leírást
                    JPanel buttonPanel = new JPanel();
                    buttonPanel.setLayout(new FlowLayout());

                    // DeleteEventActionListener hozzáadása
                    deleteButton.addActionListener(new DeleteEventActionListener(
                            currentEvent, // Az aktuális esemény részletei
                            monthView,         // Havi nézet táblázata
                            weekView           // Heti nézet táblázata
                    ));

                    editButton.addActionListener(new EditEventActionListener(
                            currentEvent,
                            monthView,
                            weekView
                    ));
                    Timer refreshTimer = new Timer(500, refreshEvent -> {
                        if (EditEventActionListener.savedInput) {
                            titleLabel.setText("Title: " + currentEvent.name);
                            detailsLabel.setText("Details: " + currentEvent.details);
                            EditEventActionListener.savedInput = false; // Reset the state
                        }
                    });
                    refreshTimer.start();
                    buttonPanel.add(deleteButton);
                    buttonPanel.add(editButton);
                    popup.add(buttonPanel, BorderLayout.SOUTH);
                }


            }else{
                popup.setSize(230, 150);
                popup.add(new JLabel("Display:  "+currentValue), BorderLayout.CENTER);
            }
            popup.setResizable(false);
            popup.setVisible(true);
        });
    }

    @Override
    public Object getCellEditorValue() {
        return currentValue.toString();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentValue = value.toString(); // Az aktuális cella értékének mentése
        return button; // Gombként jelenítjük meg a cellát
    }
}
