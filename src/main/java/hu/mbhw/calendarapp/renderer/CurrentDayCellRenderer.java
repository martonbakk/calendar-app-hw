package hu.mbhw.calendarapp.renderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDate;

/**
 * The CurrentDayCellRenderer class highlights the current day in a JTable.
 * It extends DefaultTableCellRenderer to customize the cell rendering for calendar tables.
 */
public class CurrentDayCellRenderer extends DefaultTableCellRenderer {

    // The current date used to determine which day to highlight.
    private final LocalDate today;

    // A ComboBox that indicates the currently selected month in the calendar.
    private JComboBox<String> monthComboBox;

    /**
     * Constructor for the CurrentDayCellRenderer.
     * Initializes the renderer with the current date and a reference to the month ComboBox.
     *
     * @param monthComboBox - The ComboBox used to select the month in the calendar.
     */
    public CurrentDayCellRenderer(JComboBox<String> monthComboBox) {
        this.today = LocalDate.now(); // Get the current date.
        this.monthComboBox = monthComboBox;
    }

    /**
     * Overrides the getTableCellRendererComponent method to customize the rendering of table cells.
     * Highlights the cell corresponding to the current day with a red background.
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

        // Check if the cell contains a value.
        if (value != null) {
            String cellValue = value.toString();
            try {
                // Assume the cell contains a day value as the first part of the string.
                int day = Integer.parseInt(cellValue.split(" ")[0]);

                // Check if the day matches the current day and the selected month matches today's month.
                if (day == today.getDayOfMonth() && today.getMonthValue() == monthComboBox.getSelectedIndex() + 1) {
                    // Highlight the cell background with red for the current day.
                    cell.setBackground(Color.RED);
                } else {
                    // Set the background to white for non-current days.
                    cell.setBackground(Color.WHITE);
                }
            } catch (NumberFormatException e) {
                // If the cell value is not a number, use the default white background.
                cell.setBackground(Color.WHITE);
            }
        } else {
            // For empty cells, use the default white background.
            cell.setBackground(Color.WHITE);
        }

        return cell; // Return the customized cell component.
    }
}
