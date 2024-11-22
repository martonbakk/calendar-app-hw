package hu.mbhw.calendarapp.CalendarGraphics;

import hu.mbhw.calendarapp.data.Event;
import javax.swing.*;
import java.awt.*;
import java.util.List;

import static hu.mbhw.calendarapp.data.DataHandler.getCurrentlyDisplayedEvents;

public class WeekView {
    private List<Event> data;   // Az események listája
    private JPanel mainPanel;  // Panel, amire az egészet rajzoljuk
    private JButton leftButton;
    private JButton rightButton;
    private JLabel weekLabel;  // A hét napjainak megjelenítésére
    private int currentWeekStart; // Az aktuális hét hétfőjének dátuma
    private JTable weekTable; // A táblázat az eseményekkel
    private JScrollPane scrollPaneWeekTable;

    public WeekView() {
        String[] columnNames = {"TIME", "H", "K", "Sz", "Cs", "P", "Szo", "V"}; // Az oszlopok nevei
        String[][] weekMatrix = new String[24][8]; // A hét mátrix (nap, 2 órás intervallumok)

        currentWeekStart = 1; // Az aktuális hét hétfője
        weekLabel = new JLabel(); // Címke a hét napjainak kijelzésére
        updateWeekLabel(); // Frissítjük a hét napjainak kijelzését
        weekLabel.setHorizontalAlignment(SwingConstants.CENTER);
        weekLabel.setVerticalAlignment(SwingConstants.CENTER);

        mainPanel = new JPanel(new BorderLayout()); // BorderLayoutos panel
        leftButton = new JButton("Prev");
        rightButton = new JButton("Next");

        setupNavigationButtons(); // Beállítjuk a navigációs gombokat

        // Inicializáljuk a hét mátrixot (időintervallumok és események feltöltése)
        fillWeekMatrix(weekMatrix);

        // Létrehozzuk a táblázatot
        weekTable = new JTable(weekMatrix, columnNames);
        weekTable.setRowHeight(40); // Nagyobb sorok az olvashatóságért

        scrollPaneWeekTable = new JScrollPane(weekTable);
        scrollPaneWeekTable.setPreferredSize(new Dimension(450, 220)); // Példa méret
        mainPanel.add(scrollPaneWeekTable, BorderLayout.CENTER);

        // Panel felépítése
        mainPanel.setMaximumSize(new Dimension(100, 200));
        mainPanel.add(weekLabel, BorderLayout.NORTH); // A hét napjainak megjelenítése
        mainPanel.add(scrollPaneWeekTable, BorderLayout.CENTER); // Események táblázata
        mainPanel.add(leftButton, BorderLayout.WEST); // Bal gomb
        mainPanel.add(rightButton, BorderLayout.EAST); // Jobb gomb
    }

    // Frissíti a hét napjainak kijelzését a felső címkén
    private void updateWeekLabel() {
        StringBuilder weekText = new StringBuilder("Heti napok: ");
        int tempDate = currentWeekStart;
        for (int i = 0; i < 7; i++) {
            if(tempDate<=30) {
                weekText.append(tempDate).append(" ");
                tempDate++;
            }else{
            tempDate=1;
            }
        }
        weekLabel.setText(weekText.toString());

    }



    // Beállítja a navigációs gombokat
    private void setupNavigationButtons() {
        leftButton.addActionListener(e -> {
            if(currentWeekStart>=8) {
                currentWeekStart -=7; // Előző hét
                updateWeekLabel(); // Frissítjük a címkét
                refreshWeekTable(); // Frissítjük a táblázatot
            }
        });

        rightButton.addActionListener(e -> {
            if(currentWeekStart<29) {
                currentWeekStart += 7; // Következő hét
                updateWeekLabel(); // Frissítjük a címkét
                refreshWeekTable(); // Frissítjük a táblázatot
            }
        });
    }

    private void fillWeekMatrix(String[][] weekMatrix) {
        // Alap mátrix inicializálása
        int clock = 1;
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 8; j++) {
                if (j == 0) { // Időintervallumok oszlopa
                    if (clock < 10) {
                        weekMatrix[i][j] = "0" + (clock - 1) + ":00";
                    } else {
                        weekMatrix[i][j] = (clock - 1) + ":00";
                    }
                    clock++;
                } else {
                    weekMatrix[i][j] = " "; // Események helye
                }

            }

        }

        // Események hozzárendelése a mátrixhoz
        List<Event> events = getCurrentlyDisplayedEvents(); // Az események lekérése
        for (Event event : events) {
            int columnIndex = (event.day - currentWeekStart) + 1; // +1 az időintervallum oszlop miatt
            if (columnIndex >= 1 && columnIndex <= 7) { // Csak a hét napjaira írás
                weekMatrix[event.hour - 1][columnIndex] = event.toString();
            }
        }
    }





    // Frissíti a táblázat adatait
    public void refreshWeekTable() {
        mainPanel.remove(scrollPaneWeekTable); // Előző táblázat eltávolítása
        String[][] weekMatrix = new String[24][8];
        fillWeekMatrix(weekMatrix); // Újra kitöltjük a mátrixot
        weekTable = new JTable(weekMatrix, new String[]{"TIME", "H", "K", "Sz", "Cs", "P", "Szo", "V"});
        weekTable.setRowHeight(40); // Nagyobb sorok az olvashatóságért
        scrollPaneWeekTable = new JScrollPane(weekTable);
        scrollPaneWeekTable.setPreferredSize(new Dimension(450, 220));
        mainPanel.add(scrollPaneWeekTable, BorderLayout.CENTER); // Új táblázat hozzáadása
        mainPanel.revalidate(); // Panel frissítése
        mainPanel.repaint();
    }

    public JPanel getPanel() {
        return mainPanel; // Visszaadja a fő panelt
    }
}
