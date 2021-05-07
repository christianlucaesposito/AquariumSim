package ui;

import javax.swing.table.DefaultTableModel;

/*
 * Represents the StatusTable model for status panel extends default table;
 * for an un-editable table;
 */
public class StatusTableModel extends DefaultTableModel {

    // EFFECTS: Returns whether specific cell is editable; always returns false
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

}
