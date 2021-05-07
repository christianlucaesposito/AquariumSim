package persistance;

import model.Aquarium;
import model.Fish;
import static model.Fish.*;

import exception.IllegalAquariumException;
import exception.IllegalFishException;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for JsonReader,
 * code based on JsonSerializationDemo, implementation from CPSC 210 UBC.
 **/
public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderExceptionFile() {
        JsonReader reader = new JsonReader("./data/exceptionError.json");
        try {
            Aquarium aquarium = reader.read();
            fail("Excepted exception expected but not thrown");
        } catch (IOException e) {
            // excepted
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        } catch (IllegalAquariumException e) {
            fail("Unexpected exception for aquarium");
        }
    }

    @Test
    void testReaderAquariumExceptionFile() {
        JsonReader reader = new JsonReader("./data/testReaderAquariumException.json");
        try {
            Aquarium aquarium = reader.read();
            fail("Excepted exception expected but not thrown");
        } catch (IOException e) {
            fail("Unexpected exception");
        } catch (IllegalFishException e) {
            fail("Unexpected exception for fish");
        } catch (IllegalAquariumException e) {
            // expected
        }
    }

    @Test
    void testReaderFishExceptionFile() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralAquariumException.json");
        try {
            Aquarium aquarium = reader.read();
            fail("Excepted exception expected but not thrown");
        } catch (IOException e) {
            fail("Unexpected exception");
        } catch (IllegalFishException e) {
            // expected
        } catch (IllegalAquariumException e) {
            fail("Unexpected exception for aquarium");
        }
    }

    @Test
    void testReaderEmptyAquarium() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyAquarium.json");
        try {
            Aquarium aquarium = reader.read();
            assertEquals(30, aquarium.getCleanness());
            assertEquals(0, aquarium.getFishList().size());
        } catch (IOException e) {
            fail("Unexpected exception, could not read file");
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        } catch (IllegalAquariumException e) {
            fail("Unexpected exception for aquarium");
        }
    }

    @Test
    void testReaderGeneralAquarium() {

        JsonReader reader = new JsonReader("./data/testReaderGeneralAquarium.json");
        try {
            Aquarium aquarium = reader.read();
            assertEquals(25, aquarium.getCleanness());
            ArrayList<Fish> fishList = aquarium.getFishList();
            assertEquals(2, aquarium.getFishList().size());
            checkFish(ALIVE, 2, 50, 1, "Penny", fishList.get(0));
            checkFish(DEAD, 15, 15, 2, null, fishList.get(1));
        } catch (IOException e) {
            fail("Unexpected exception, could not read file " + e.getMessage());
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        } catch (IllegalAquariumException e) {
            fail("Unexpected exception for aquarium");
        }
    }
}
