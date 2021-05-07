package ui;

import model.Aquarium;
import model.Fish;
import exception.IllegalAquariumException;
import exception.IllegalFishException;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/*
 * Text based console UI for running application
 */
public class TextBasedCommandUI {
    private static final String JSON_STORE = "./save/aquarium.json";
    private Scanner input;
    private Aquarium aquarium;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: run the Aquarium application
    public TextBasedCommandUI() {
        runAquariumApp();
    }

    // MODIFIES: this
    // EFFECTS: initializes aquarium
    public void init() {
        aquarium = new Aquarium();
        input = new Scanner(System.in);
    }

    // MODIFIES: this
    // EFFECTS: run processes user input
    private void runAquariumApp() {
        boolean keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nEmptying tank, and killing all fish! Goodbye...");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("a")) {
            processNewFish();

        } else if (command.equals("f")) {
            try {
                processFishFeed();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else if (command.equals("c")) {
            try {
                processCleaning();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else if (command.equals("s")) {
            processStatus();

        } else if (command.equals("t")) {
            processPassTime();

        } else if (command.equals("o")) {
            processSaveCommands(command);

        } else {
            System.out.println("That's nonsense.. Your selection is not valid!");
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user command for saving and opening save file
    private void processSaveCommands(String command) {
        boolean keepGoing = true;

        while (keepGoing) {
            displaySaveMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("b")) {
                keepGoing = false;
            } else if (command.equals("s")) {
                processSaving();
                keepGoing = false;
            } else if (command.equals("o")) {
                processOpen();
                keepGoing = false;
            } else {
                System.out.println("hm.. Awkward.. your selection is not valid..");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: reads the saved file and loads it to aquarium
    private void processOpen() {
        JsonReader jsonReader = new JsonReader(JSON_STORE);
        try {
            aquarium = jsonReader.read();
            System.out.println("Loaded Aquarium  from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Save file not found, no file in " + JSON_STORE);
        } catch (IllegalFishException e) {
            System.out.println("Corrupted save");
        } catch (IllegalAquariumException e) {
            System.out.println("Corrupted save");
        }
    }

    // EFFECTS: saves the aquarium to file
    private void processSaving() {
        JsonWriter jsonWriter = new JsonWriter(JSON_STORE);
        try {
            jsonWriter.write(aquarium);
        } catch (FileNotFoundException e) {
            System.out.println("Error: Unable to save! " + e.getMessage());
            return;
        }
        System.out.println("Saved aquarium to " + JSON_STORE);
    }

    // MODIFIES: this
    // EFFECTS: Processes time passing and displays text for passage of time
    private void processPassTime() {
        aquarium.passTime();
        aquarium.passTime();
        aquarium.passTime();
        System.out.println("Some time has passed...");
    }

    // EFFECTS: Outputs the status of the aquarium, and fish
    private void processStatus() {
        System.out.println(aquarium.toString());
    }

    // MODIFIES: this
    // EFFECTS: Process aquarium cleaning and displays cleaning text
    private void processCleaning() throws InterruptedException {
        if (aquarium.isFishListEmpty()) {
            System.out.println("\nCleaning empty tank...");
            Thread.sleep(500);
            aquarium.clean();
            System.out.println("\nAll done!");

        } else if (aquarium.getCleanness() == Aquarium.MAX_CLEANNESS_LEVEL && !aquarium.isAnyFishDead()) {
            System.out.println("\nNo need to clean! All fish are alive and tank is clean...");

        } else if (aquarium.getCleanness() == Aquarium.MAX_CLEANNESS_LEVEL && aquarium.isAnyFishDead()) {
            displayThreeTextWithSleep("\nGoodbye dead fishy...", 100, 500, "Removing all dead fish...",
                    "\nAll clean! All dead fish has been removed...'");
            aquarium.clean();

        } else if (aquarium.getCleanness() != Aquarium.MAX_CLEANNESS_LEVEL && aquarium.isAnyFishDead()) {
            displayThreeTextWithSleep("\nCleaning tank and removing dead fish...", 500, 500,
                    "Cleaning tank and removing dead fish...",
                    "\nAll clean! All clean... bye bye dead fishy...");
            aquarium.clean();

        } else {
            displayThreeTextWithSleep("\nStarting tank cleaning...", 100, 200,
                    "Cleaning tank...",  "\nAll clean!");
            aquarium.clean();
        }
    }

    // EFFECTS: Prints s, s2, and s3 string with p and p2 pauses
    private void displayThreeTextWithSleep(String s, int p, int p2, String s2, String s3) throws InterruptedException {
        System.out.println(s);
        Thread.sleep(p);
        System.out.println(s2);
        Thread.sleep(p2);
        System.out.println(s2);
        Thread.sleep(p2);
        System.out.println(s3);
    }

    // MODIFIES: this
    // EFFECTS: processes fish feeding and displays feeding text
    private void processFishFeed() throws InterruptedException {
        if (aquarium.isFishListEmpty()) {
            System.out.println("\nThere are no fish to feed!");
        } else if (!aquarium.isAnyFishAlive()) {
            System.out.println("\nThere are no alive fish to feed!");
        } else {
            aquarium.feedAllFish();
            System.out.println("\nfeeding...");
            Thread.sleep(500);
            System.out.println("feeding...");
            Thread.sleep(500);
            System.out.println("feeding...");
            Thread.sleep(500);
            System.out.println("\n All the fish have been fed!");
            Thread.sleep(500);
        }
    }

    // MODIFIES: this
    // EFFECTS: Process adding new fish and displays text for new fish, including naming
    private void processNewFish() {
        boolean keepGoing = true;
        String command = null;
        try {
            aquarium.addFish();
        } catch (IllegalFishException e) {
            System.out.println("Fish stats not allowed. Internal error");
            return;
        }
        System.out.println("\nA brand new fish has been added to the tank!");
        displayNewFishMenu();

        while (keepGoing) {
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("n")) {
                keepGoing = false;
            } else if (command.equals("y")) {
                processNameFish();
                keepGoing = false;
            } else {
                System.out.println("Hmm... your selection is not valid, pick y or n!");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user command for naming new fish and display text
    private void processNameFish() {
        Fish fish = aquarium.getFishList().get(aquarium.getFishList().size() - 1);
        String command;
        boolean keepPickingName = true;
        System.out.println("\n Pick the name of your new fish!");

        while (keepPickingName) {
            System.out.println("\n What is their name?");
            command = input.next();
            fish.setName(command);
            boolean keepGoing = true;

            while (keepGoing) {
                displayNameConfirmation(fish);
                command = input.next();
                if (command.equals("y")) {
                    keepPickingName = false;
                    keepGoing = false;
                } else if (command.equals("n")) {
                    keepGoing = false;
                } else {
                    System.out.println("\nSelection not valid...");
                }
            }
        }
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> add new fish");
        System.out.println("\tf -> feed fish");
        System.out.println("\tc -> clean aquarium");
        System.out.println("\ts -> fish status");
        System.out.println("\tt -> pass time");
        System.out.println("\to -> save options");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: displays save menu of options to user
    private void displaySaveMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ts -> save to file");
        System.out.println("\to -> open save state");
        System.out.println("\tb -> back");
    }

    // EFFECTS: displays new fish menu of options to user
    private void displayNewFishMenu() {
        System.out.println("\n Would you like to name your fish?");
        System.out.println("\ty -> Yes");
        System.out.println("\tn -> Nope");
    }

    // EFFECTS: displays menu of options to user for name confirmation
    private void displayNameConfirmation(Fish fish) {
        System.out.println("\n Name your fish " + fish.getName() + "?");
        System.out.println("\ty -> Yes");
        System.out.println("\tn -> No");
    }
}
