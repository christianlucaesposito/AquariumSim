package persistence;

import model.Aquarium;
import org.json.JSONObject;
import java.io.*;

/*
Represents a writer that writes JSON representation of Aquarium to file
 * destination represents the destination of file;
 * code based on JsonSerializationDemo, implementation from CPSC 210 UBC.
 */
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs a writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; if destination file cannot be opened throws FileNotFoundException
    public void openWriter() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void closeWriter() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of workroom to file
    public void write(Aquarium aquarium) throws FileNotFoundException {
        openWriter();
        JSONObject json = aquarium.toJson();
        saveToFile(json.toString(TAB));
        closeWriter();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
