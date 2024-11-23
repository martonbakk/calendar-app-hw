package hu.mbhw.calendarapp.renderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDate;

public class CurrentDayCellRenderer extends DefaultTableCellRenderer {
    private final LocalDate today;
    private JComboBox<String> monthComboBox;

    public CurrentDayCellRenderer(JComboBox<String> monthComboBox) {
        this.today = LocalDate.now();
        this.monthComboBox=monthComboBox;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Ellenőrizd, hogy a cella tartalmazza-e az aktuális napot
        if (value != null) {
            String cellValue = value.toString();
            try {
                // Az aktuális hónap napjait feltételezve ellenőrizd a napot
                int day = Integer.parseInt(cellValue.split(" ")[0]);
                if (day == today.getDayOfMonth() && today.getMonthValue() == monthComboBox.getSelectedIndex() + 1) {
                    cell.setBackground(Color.RED);  // Állítsd a háttérszínt egyedi színre
                } else {
                    cell.setBackground(Color.WHITE); // Állítsd vissza az alapértelmezett színt
                }
            } catch (NumberFormatException e) {
                cell.setBackground(Color.WHITE); // Nem számokat tartalmazó cellák alapértelmezett színe
            }
        } else {
            cell.setBackground(Color.WHITE); // Üres cellák színe
        }

        return cell;
    }
}
