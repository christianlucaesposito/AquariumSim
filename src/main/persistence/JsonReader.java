package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import model.Aquarium;
import model.Fish;
import exception.IllegalAquariumException;
import exception.IllegalFishException;
import org.json.JSONArray;
import org.json.JSONObject;

/*
 * Represents an reader to read source file, to enable data to be loaded;
 * contains source, which represents the location of file;
 * code based on JsonSerializationDemo, implementation from CPSC 210 UBC.
 */
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    public Aquarium read() throws IOException, IllegalAquariumException, IllegalFishException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseAquarium(jsonObject);
    }

    // EFFECTS: parses aquarium from JSON object and returns it
    // throws IllegalAquariumException or IllegalFIshAquarium if illegal parameter values are given
    private Aquarium parseAquarium(JSONObject jsonObject) throws IllegalAquariumException, IllegalFishException {
        int cleanness = jsonObject.getInt("cleanness");
        Aquarium aquarium = new Aquarium(cleanness);
        addFishList(aquarium, jsonObject);
        return aquarium;
    }

    // MODIFIES: aquarium
    // EFFECTS: parses fishList from JSON object and adds them to aquarium
    // throws IllegalFIshAquarium if illegal parameter values are given
    private void addFishList(Aquarium aquarium, JSONObject jsonObject) throws IllegalFishException {
        JSONArray jsonArray = jsonObject.getJSONArray("fishList");
        for (Object json : jsonArray) {
            JSONObject nextFish = (JSONObject) json;
            addFish(aquarium, nextFish);
        }
    }

    // MODIFIES: aquarium
    // EFFECTS: parses fish from JSON object and adds it to aquarium
    // throws IllegalAquariumException are given
    private void addFish(Aquarium aquarium, JSONObject jsonObject) throws IllegalFishException {
        String name = jsonObject.optString("name", null);
        int status = jsonObject.getInt("status");
        int hungerLevel = jsonObject.getInt("hungerLevel");
        int growthTimer = jsonObject.getInt("growthTimer");
        int size = jsonObject.getInt("size");

        Fish fish = new Fish(size, hungerLevel, status, growthTimer, name);
        aquarium.addFish(fish);
    }

    // EFFECTS: reads source file as string and returns it, throws IOException if cannot read file
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }
}
