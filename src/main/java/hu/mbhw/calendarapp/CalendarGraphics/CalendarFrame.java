package hu.mbhw.calendarapp.CalendarGraphics;

import hu.mbhw.calendarapp.actionlisteners.*;
import hu.mbhw.calendarapp.renderer.InteractiveCellEditor;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class CalendarFrame extends JFrame {
    private JPanel lowPanel;
    private JPanel currentLowPanel;

    private MenuBar menuBar;
    private MonthView monthView;
    private WeekView weekView;
    private InteractiveCellEditor interactiveCellEditor;

    public static Enums.CALENDARFRAME_VIEW currentView = Enums.CALENDARFRAME_VIEW.MONTH;

    public CalendarFrame(LocalDate localDate) {
        setTitle("Calendar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 330);
        setResizable(false);

        // MenuBar setup
        menuBar = new MenuBar();

        // Initialize views and dialogs
        monthView = new MonthView(localDate);
        weekView = new WeekView(localDate, monthView.getMonthComboBox());

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
        menuBar.getAddEventButton().addActionListener(new AddEventButtonListener(menuBar, monthView, weekView));
    }

    public void toggleView(){
        lowPanel.remove(currentLowPanel);
        System.out.println("SWITCH VIEW: "+ currentView.name());
        if (currentView==Enums.CALENDARFRAME_VIEW.MONTH) {
            currentView = Enums.CALENDARFRAME_VIEW.WEEK;
            // Switch to Week View
            currentLowPanel = new JPanel();
            weekView.updateWeekTable();
            currentLowPanel.add(weekView.getPanel(), BorderLayout.CENTER);
        } else {
            currentView = Enums.CALENDARFRAME_VIEW.MONTH;
            // Switch to Month View
            monthView.updateEventList();
            monthView.updateEventMatrix();
            currentLowPanel = new JPanel(new BorderLayout());
            currentLowPanel.add(monthView.getPanel(), BorderLayout.CENTER);
        }

        // Revalidate and repaint the low panel to reflect the changes
        lowPanel.add(currentLowPanel, BorderLayout.CENTER);
        lowPanel.revalidate();
        lowPanel.repaint();
    }

}

