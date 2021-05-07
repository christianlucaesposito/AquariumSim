package ui;

import model.Aquarium;
import exception.IllegalAquariumException;
import exception.IllegalFishException;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

/*
 * Represents panel for button commands;
 * Command panel that allows saving, loading, feeding, adding fish, and cleaning tank
 */
public class ButtonCommandPanel extends JPanel implements ActionListener {
    private static final String ADD_FISH = "addFish";
    private static final String FEED_FISH = "feedFish";
    private static final String CLEAN_TANK = "clean";
    private static final String PASS_TIME = "passTime";
    private static final String SAVE = "save";
    private static final String LOAD = "load";

    private static final String JSON_STORE = "./save/aquarium.json";
    private final GUI gui;

    // EFFECTS: constructs button command panel
    public ButtonCommandPanel(GUI gui) {
        super();
        this.gui = gui;
        addAllButtons();
    }

    @Override
    // This is the method that is called when the the JButton btn is clicked
    // MODIFIES: this
    // EFFECT: processes action performed
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() != null) {
            switch (e.getActionCommand()) {
                case ADD_FISH:
                    processNewFish();
                    break;
                case FEED_FISH:
                    processFishFeed();
                    break;
                case CLEAN_TANK:
                    processCleaning();
                    break;
                case PASS_TIME:
                    processPassTime();
                    break;
                case SAVE:
                    processSaving();
                    break;
                case LOAD:
                    processLoading();
                    break;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Processes time passing
    private void processPassTime() {
        gui.getAquarium().passTime();
        gui.getStatusPanel().updateFishTable();
    }

    // MODIFIES: this
    // EFFECTS: Process aquarium cleaning
    private void processCleaning() {
        if (gui.getAquarium().getCleanness() == Aquarium.MAX_CLEANNESS_LEVEL) {
            JOptionPane.showMessageDialog(gui, "No need to clean tank!");
        } else if (gui.getAquarium().isFishListEmpty()) {
            gui.getAquarium().clean();
            messagePopUp("Tank cleaned!");
        } else if (!gui.getAquarium().isAnyFishDead() && !gui.getAquarium().isFishListEmpty()) {
            gui.getAquarium().clean();
            messagePopUp("Tank cleaned! All fish are alive and tank is clean...");
        } else if  (gui.getAquarium().isAnyFishDead()) {
            gui.getAquarium().clean();
            messagePopUp("Tank cleaned! All dead fish have been removed...");
        }
        gui.getStatusPanel().updateFishTable();
    }

    // MODIFIES: this
    // EFFECTS: displays info message dialogue message
    private void messagePopUp(String message) {
        JOptionPane.showMessageDialog(gui,
                message,
                "Info message",
                JOptionPane.PLAIN_MESSAGE);
    }

    // MODIFIES: this and AquariumRenderPanel
    // EFFECTS: reads the saved file and loads it to aquarium
    // pops error window in case of exception
    private void processLoading() {
        JsonReader jsonReader = new JsonReader(JSON_STORE);
        try {
            gui.setAquarium(jsonReader.read());
            JOptionPane.showMessageDialog(gui, "Loaded Aquarium  from " + JSON_STORE,
                    "Successful Load", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(gui,"Error: Unable to load save: " + e.getMessage(),
                    "Load Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalFishException e) {
            JOptionPane.showMessageDialog(gui, "Corrupted Aquarium save file. Unable to load save file.",
                    "Corruption Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalAquariumException e) {
            JOptionPane.showMessageDialog(gui, "Corrupted save file. Unable to load save fine.",
                    "Corruption Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // EFFECTS: saves the aquarium to file, and pops up message
    private void processSaving() {
        JsonWriter jsonWriter = new JsonWriter(JSON_STORE);
        try {
            jsonWriter.write(gui.getAquarium());
            JOptionPane.showMessageDialog(
                    gui,
                    "Aquarium has been saved to " + JSON_STORE,
                    "Successful Save",
                    JOptionPane.PLAIN_MESSAGE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(
                    gui,
                    "Error: Unable to save! " + e.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: processes fish feeding and displays feeding visual and messages
    private void processFishFeed() {
        if (gui.getAquarium().isFishListEmpty()) {
            JOptionPane.showMessageDialog(
                    gui,
                    "There are no fish to feed!",
                    "Feeding Error",
                    JOptionPane.WARNING_MESSAGE);
        } else if (!gui.getAquarium().isAnyFishAlive()) {
            JOptionPane.showMessageDialog(
                    gui,
                    "There are no alive fish to feed :(",
                    "Feeding Error",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            gui.getAquarium().feedAllFish();
            gui.getStatusPanel().updateFishTable();
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user command for naming new fish and displays it
    private void processNewFish() {
        try {
            gui.getAquarium().addFish();
        } catch (IllegalFishException e) {
            JOptionPane.showMessageDialog(
                    gui, "Internal code implementation error",
                    "Critical Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        gui.getStatusPanel().updateFishTable();
        int option = JOptionPane.showConfirmDialog(gui, "Would you like to name your fish?",
                "New fish options", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            String name = (String) JOptionPane.showInputDialog(
                    gui, "What is their name?", "Fish naming menu", JOptionPane.QUESTION_MESSAGE,
                    null, null, "");

            // names the fish
            gui.getAquarium().getLastFish().setName(name);
            gui.getStatusPanel().updateFishTable();
        }
    }

    // MODIFIES: this
    // EFFECT: adds all buttons and sets "this" object as an action listener for btn
    private void addAllButtons() {
        addButton("Add Fish", ADD_FISH);
        addButton("Feed Fish", FEED_FISH);
        addButton("Clean Aquarium", CLEAN_TANK);
        addButton("Pass Time", PASS_TIME);
        addButton("Save", SAVE);
        addButton("Load", LOAD);
    }

    // MODIFIES: this
    // EFFECT: Creates a buttons  with a name and action command,
    // sets "this" object as an action listener for btn
    private void addButton(String buttonName, String actionCommand) {
        JButton btn = new JButton(buttonName);
        btn.setActionCommand(actionCommand);
        btn.addActionListener(this);
        add(btn);
    }
}
