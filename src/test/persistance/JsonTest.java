package persistance;

import model.Fish;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkFish(int status, int hungerLevel, int growthTimer, int size, String name, Fish fish) {
        assertEquals(status, fish.getStatus());
        assertEquals(hungerLevel, fish.getHungerLevel());
        assertEquals(growthTimer, fish.getGrowthTimer());
        assertEquals(size, fish.getSize());
        assertEquals(name, fish.getName());
    }
}
