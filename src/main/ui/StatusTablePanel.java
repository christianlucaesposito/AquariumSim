package ui;

import model.Aquarium;
import model.Fish;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

/*
 * Represents the status panel with fish stats;
 * holds Jtable with fish status and name of columns,
 * and its respective aquarium
 */
public class StatusTablePanel extends JPanel {
    private JTable fishListTable;
    private Vector<String> fishStatusColumnNames;
    private Aquarium aquarium;

    // MODIFIES: this
    // EFFECTS: constructs a status table panel, and sets aquarium in field
    public StatusTablePanel(Aquarium aquarium) {
        super();
        setLayout(new BorderLayout());
        this.aquarium = aquarium;
        setUpTable();
    }

    // MODIFIES: this
    // EFFECTS: creates fish status panel
    private void setUpTable() {
        // Data for table and column Names
        fishStatusColumnNames = new Vector<>();
        fishStatusColumnNames.add("Name");
        fishStatusColumnNames.add("Hunger");
        fishStatusColumnNames.add("Status");

        fishListTable = new JTable(new StatusTableModel());
        updateFishTable();
        add(fishListTable);
        fishListTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane sp = new JScrollPane(fishListTable);
        add(sp);
    }

    // MODIFIES: this
    // EFFECT: Updated the fish table with current values
    public void updateFishTable() {
        ArrayList<Fish> fishList = aquarium.getFishList();
        Vector<Vector<String>> fishData = new Vector<>();

        for (Fish fish : fishList) {
            Vector<String> row = new Vector<>();

            row.add(fish.getName());
            row.add(fish.hungerToString());
            row.add(fish.getStatus() == 0 ? "Dead" : "Alive");

            fishData.add(row);
        }

        DefaultTableModel model = (DefaultTableModel) fishListTable.getModel();
        model.setDataVector(fishData, fishStatusColumnNames);
    }

    // MODIFIES: this
    // EFFECTS: Sets given aquarium as aquarium
    public void setAquarium(Aquarium aquarium) {
        this.aquarium = aquarium;
    }
}
