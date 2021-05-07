package persistence;

import org.json.JSONObject;

// Represents Interface; meant for turning object into Json object
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}