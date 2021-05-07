package persistance;

import model.Aquarium;
import model.Fish;
import exception.IllegalAquariumException;
import exception.IllegalFishException;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;

import static model.Fish.ALIVE;
import static model.Fish.DEAD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for JsonWriter,
 * code based on JsonSerializationDemo, from CPSC 210 UBC.
 **/
public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Aquarium aq = new Aquarium();
            JsonWriter writer = new JsonWriter("./save/my\0illigalName.json");
            writer.openWriter();
            fail("Missing exception");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    void testWriterEmptyAquarium() {
        try {
            Aquarium aq = new Aquarium();
            JsonWriter wr = new JsonWriter("./data/testWriterEmptyAquarium.json");
            wr.write(aq);

            JsonReader reader = new JsonReader("./data/testWriterEmptyAquarium.json");
            aq = reader.read();
            assertEquals(30, aq.getCleanness());
            assertEquals(0, aq.getFishList().size());
        } catch (IOException e) {
            fail("Unexpected exception");
        } catch (IllegalFishException e) {
            fail("Unexpected exception for fish");
        } catch (IllegalAquariumException e) {
            fail("Unexpected exception for aquarium");
        }
    }

    @Test
    void testWriterGeneralAquarium() {
        try {
            Aquarium aq = new Aquarium(25);
            aq.addFish(new Fish(1, 2, ALIVE, 50, "Penny"));
            aq.addFish(new Fish(2, 15, DEAD, 15, null));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralAquarium.json");
            writer.write(aq);

            JsonReader reader = new JsonReader("./data/testWriterGeneralAquarium.json");
            aq = reader.read();
            assertEquals(25, aq.getCleanness());
            ArrayList<Fish> fishList = aq.getFishList();
            assertEquals(2, aq.getFishList().size());
            checkFish(ALIVE, 2, 50, 1, "Penny", fishList.get(0));
            checkFish(DEAD, 15, 15, 2, null, fishList.get(1));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        } catch (IllegalAquariumException e) {
            fail("Unexpected exception");
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
    }
}
