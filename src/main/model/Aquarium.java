package model;

import exception.IllegalAquariumException;
import exception.IllegalFishException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

import static model.Fish.*;

/*
 * Represents an aquarium having cleanness level, and list of fish;
 * cleanness is the aquarium cleanness level, with a minimum and maximum value;
 * fishList is the list of fish present inside the aquarium;
 */
public class Aquarium implements Writable {
    public static final int MAX_CLEANNESS_LEVEL = 30;
    public static final int MIN_CLEANNESS_LEVEL = 0;
    private int cleanness;
    private final ArrayList<Fish> fishList;

    // EFFECTS: constructs an aquarium; sets cleanness to MAX_CLEANNESS_LEVEL; sets up fishList
    public Aquarium() {
        cleanness = MAX_CLEANNESS_LEVEL;
        fishList = new ArrayList<>();
    }

    // EFFECTS: constructs an aquarium; sets cleanness to cleanness; sets up fishList
    // throws IllegalAquariumException if cleanness not valid
    public Aquarium(int cleanness) throws IllegalAquariumException {
        if (cleanness > MAX_CLEANNESS_LEVEL || cleanness < MIN_CLEANNESS_LEVEL) {
            throw new IllegalAquariumException();
        }
        this.cleanness = cleanness;
        fishList = new ArrayList<>();
    }

    // EFFECTS: returns aquarium cleanness value
    public int getCleanness() {
        return cleanness;
    }

    // EFFECTS: returns list of fish in aquarium
    public ArrayList<Fish> getFishList() {
        return fishList;
    }

    // MODIFIES: this
    // EFFECTS: decreases cleanness of tank by 1 down to a minimum of MIN_CLEANNESS_LEVEL,
    // for alive fish increases hunger of fish in list by 1,
    // and decreases fish growth timer by 1,
    // if fish timer ends grows and resets the timer,
    // if fish reaches FISH_HUNGER_TO_STARVE fish dies starved, and does not grow even if timer reaches zero
    public void passTime() {
        cleanness = Math.max(cleanness - 1, MIN_CLEANNESS_LEVEL);

        for (Fish fish : fishList) {
            fish.passTime();
        }
    }

    // MODIFIES: this
    // EFFECTS: feeds all alive fish in list of fish
    public void feedAllFish() {
        for (Fish fish : fishList) {
            fish.feed();
        }
    }

    // EFFECTS: returns true if fish list is empty, else false
    public boolean isFishListEmpty() {
        return fishList.isEmpty();
    }

    // EFFECTS: returns true if there is at least one alive fish in list of fish, else false
    public boolean isAnyFishAlive() {
        for (Fish fish : fishList) {
            if (fish.getStatus() == ALIVE) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: returns true if there is at least one dead fish in list of fish, else false
    public boolean isAnyFishDead() {
        for (Fish fish : fishList) {
            if (fish.getStatus() == DEAD) {
                return true;
            }
        }
        return false;
    }

    // EFFECT: Returns last fish added in List of fish
    public Fish getLastFish() {
        return fishList.get(fishList.size() - 1);
    }

    @Override
    // EFFECTS: Prints cleanness, and fish status, hunger and size
    public String toString() {
        StringBuilder output = new StringBuilder("Aquarium numFish=" + fishList.size() + " cleanness=" + cleanness);
        for (Fish fish : fishList) {
            output.append("\n").append(fish.toString());
        }
        return output.toString();
    }

    // MODIFIES: this
    // EFFECTS: sets the aquarium cleanness level to 10, removes all dead fish from tank
    public void clean() {
        this.cleanness = MAX_CLEANNESS_LEVEL;
        fishList.removeIf(fish -> fish.getStatus() == DEAD);
    }

    // MODIFIES: this
    // EFFECTS: adds a fish to fishList
    public void addFish(Fish fish) {
        fishList.add(fish);
    }


    // MODIFIES: this
    // EFFECTS: constructs new fish with default values, and adds to fishList
    public void addFish() throws IllegalFishException {
        addFish(FISH_MIN_SIZE, FISH_INITIAL_HUNGER_VALUE, ALIVE, GROWTH_TIMER_START_VALUE);
    }

    // MODIFIES: this
    // EFFECTS: constructs new fish with custom values and adds to fishList
    public void addFish(int size, int initialHunger, int status, int growthTimer) throws IllegalFishException {
        fishList.add(new Fish(size, initialHunger, status, growthTimer, null));
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("cleanness", cleanness);
        json.put("fishList", fishListToJson());
        return json;
    }

    // EFFECTS: returns fishList in this Aquarium as a JSON array
    private JSONArray fishListToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Fish f : fishList) {
            jsonArray.put(f.toJson());
        }

        return jsonArray;
    }
}
