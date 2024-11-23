package hu.mbhw.calendarapp.renderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDate;

public class CurrentDayColumnRenderer extends DefaultTableCellRenderer {
    private final int currentDayColumn;

    public CurrentDayColumnRenderer(int currentDayColumn) {
        this.currentDayColumn = currentDayColumn; // Az oszlop indexe, ami az aktuális napot reprezentálja
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Ellenőrizzük, hogy az aktuális nap oszlopában vagyunk-e
        if (column == currentDayColumn && !isSelected) {
            cell.setBackground(Color.CYAN); // Az aktuális nap színe
        } else {
            cell.setBackground(Color.WHITE); // Egyéb cellák alapértelmezett színe
        }

        return cell;
    }
}
