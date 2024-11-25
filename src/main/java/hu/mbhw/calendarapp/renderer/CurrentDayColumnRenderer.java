package hu.mbhw.calendarapp.renderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * The CurrentDayColumnRenderer class highlights the entire column of the current day in a JTable.
 * It extends DefaultTableCellRenderer to customize the rendering for a specific column.
 */
public class CurrentDayColumnRenderer extends DefaultTableCellRenderer {
    /**
     * Overrides the getTableCellRendererComponent method to customize the rendering of table cells.
     * Highlights the column corresponding to the current day with a red background.
     *
     * @param table - The JTable being rendered.
     * @param value - The value of the cell being rendered.
     * @param isSelected - Whether the cell is selected.
     * @param hasFocus - Whether the cell has focus.
     * @param row - The row index of the cell.
     * @param column - The column index of the cell.
     * @return The rendered cell component.
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Get the default rendering component for the cell.
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Check if the current cell is in the column representing the current day.
        if (!isSelected) {
            // Set the background color to red for the current day column.
            cell.setBackground(Color.RED);
        } else {
            // Set the background color to white for other columns.
            cell.setBackground(Color.WHITE);
        }

        return cell; // Return the customized cell component.
    }
}
