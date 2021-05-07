package model;

import static model.Aquarium.*;
import static model.Fish.*;
import static org.junit.jupiter.api.Assertions.*;

import exception.IllegalAquariumException;
import exception.IllegalFishException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * Tests for Aquarium Class
 */
public class AquariumTests {
    private Aquarium testAquarium;

    @BeforeEach
    public void setup() {
        testAquarium = new Aquarium();
    }

    @Test
    public void testAquariumConstructor() {
        assertEquals(MAX_CLEANNESS_LEVEL, testAquarium.getCleanness());
        assertTrue(testAquarium.getFishList().isEmpty());
    }

    @Test
    public void testAquariumConstructorExceptionHigh() {
        try {
            testAquarium = new Aquarium(MAX_CLEANNESS_LEVEL + 3);
            fail();
        } catch (IllegalAquariumException e) {
            // expected
        }
    }

    @Test
    public void testAquariumConstructorExceptionLow() {
        try {
            testAquarium = new Aquarium(MIN_CLEANNESS_LEVEL - 1);
            fail();
        } catch (IllegalAquariumException e) {
            // expected
        }
    }
    @Test
    public void testAquariumConstructorNoException() {
        try {
            testAquarium = new Aquarium(MAX_CLEANNESS_LEVEL);
        } catch (IllegalAquariumException e) {
            fail();
        }
    }

    @Test
    public void testToString() {
        try {
            testAquarium.addFish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        assertEquals("Aquarium numFish=1 cleanness=30\n" +
                "Fish name=null hunger=2 size=1 state=alive", testAquarium.toString());
    }

    @Test
    public void testPassTimeNoFish() {
        testAquarium.passTime();

        assertEquals(MAX_CLEANNESS_LEVEL - 1, testAquarium.getCleanness());
        assertTrue(testAquarium.getFishList().isEmpty());
    }

    @Test
    public void testPassTimeMinDirt() {
        // make aquarium most dirty
        try {
            testAquarium = new Aquarium(MIN_CLEANNESS_LEVEL);
        } catch (IllegalAquariumException e) {
            fail("Unexpected exception");
        }

        // pass time
        testAquarium.passTime();

        // test that cleanness is not below minimum cleanness with no fish
        assertEquals(MIN_CLEANNESS_LEVEL, testAquarium.getCleanness());
        assertTrue(testAquarium.getFishList().isEmpty());
    }

    @Test
    public void testAddFishOwnFish() {
        Fish testFish = null;
        try {
            testFish = new Fish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }

        // add own testFish to aquarium
        testAquarium.addFish(testFish);

        // test if fish is in fishList
        assertEquals(1, testAquarium.getFishList().size());
        assertTrue(testAquarium.getFishList().contains(testFish));
    }

    @Test
    public void testAddFishSpecified() {
        // add fish, check if all values added properly
        try {
            testAquarium.addFish(2,3, ALIVE,4);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        assertEquals(1, testAquarium.getFishList().size());
        assertEquals(3, testAquarium.getFishList().get(0).getHungerLevel());
        assertEquals(ALIVE, testAquarium.getFishList().get(0).getStatus());
        assertEquals(4, testAquarium.getFishList().get(0).getGrowthTimer());
        assertEquals(2, testAquarium.getFishList().get(0).getSize());

        // add another fish, check size
        try {
            testAquarium.addFish(2,3, ALIVE,4);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        assertEquals(2, testAquarium.getFishList().size());
    }

    @Test
    public void testAddFishGeneric() {
        try {
            testAquarium.addFish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        assertEquals(1, testAquarium.getFishList().size());
        try {
            testAquarium.addFish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        assertEquals(2, testAquarium.getFishList().size());

    }

    @Test
    public void testPassTimeWithAliveFish() {
        // Creates fish and adds to aquarium
        Fish fish = null;
        try {
            fish = new Fish(Fish.FISH_MIN_SIZE, Fish.FISH_INITIAL_HUNGER_VALUE,
                    Fish.ALIVE, Fish.GROWTH_TIMER_START_VALUE, null);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        testAquarium.addFish(fish);

        // pass time
        testAquarium.passTime();

        // test change in values, hunger and growth
        assertEquals(MAX_CLEANNESS_LEVEL - 1, testAquarium.getCleanness());
        assertEquals(1, testAquarium.getFishList().size());
        assertEquals(FISH_INITIAL_HUNGER_VALUE + 1, testAquarium.getFishList().get(0).getHungerLevel());
        assertEquals(GROWTH_TIMER_START_VALUE - 1, testAquarium.getFishList().get(0).getGrowthTimer());
        assertEquals(FISH_MIN_SIZE, testAquarium.getFishList().get(0).getSize());

    }

    @Test
    public void testPassTimeDeadFish() {
        // Creates fish and adds to aquarium
        Fish fish = null;
        try {
            fish = new Fish(Fish.FISH_MIN_SIZE, Fish.FISH_INITIAL_HUNGER_VALUE,
                    Fish.DEAD, Fish.GROWTH_TIMER_START_VALUE, null);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        testAquarium.addFish(fish);

        //pass time
        testAquarium.passTime();

        // test no change due fish status dead
        assertEquals(MAX_CLEANNESS_LEVEL - 1, testAquarium.getCleanness());
        assertEquals(1, testAquarium.getFishList().size());
        assertEquals(FISH_INITIAL_HUNGER_VALUE, testAquarium.getFishList().get(0).getHungerLevel());
        assertEquals(GROWTH_TIMER_START_VALUE, testAquarium.getFishList().get(0).getGrowthTimer());
        assertEquals(FISH_MIN_SIZE, testAquarium.getFishList().get(0).getSize());
        assertEquals(DEAD, testAquarium.getFishList().get(0).getStatus());
    }

    @Test
    public void testPassTimeDeadAndAliveFish() {
        // Creates fish and adds to aquarium
        Fish fish1 = null;
        try {
            fish1 = new Fish(Fish.FISH_MIN_SIZE, Fish.FISH_INITIAL_HUNGER_VALUE,
                    Fish.ALIVE, Fish.GROWTH_TIMER_START_VALUE, null);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        Fish fish2 = null;
        try {
            fish2 = new Fish(Fish.FISH_MIN_SIZE, Fish.FISH_INITIAL_HUNGER_VALUE,
                    Fish.DEAD, Fish.GROWTH_TIMER_START_VALUE, null);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        testAquarium.addFish(fish1);
        testAquarium.addFish(fish2);

        // pass time
        testAquarium.passTime();

        // test first fish, with change
        assertEquals(MAX_CLEANNESS_LEVEL - 1, testAquarium.getCleanness());
        assertEquals(2, testAquarium.getFishList().size());
        assertEquals(FISH_INITIAL_HUNGER_VALUE + 1, testAquarium.getFishList().get(0).getHungerLevel());
        assertEquals(GROWTH_TIMER_START_VALUE - 1, testAquarium.getFishList().get(0).getGrowthTimer());
        assertEquals(FISH_MIN_SIZE, testAquarium.getFishList().get(0).getSize());
        // test second fish, no change due status dead
        assertEquals(FISH_INITIAL_HUNGER_VALUE, testAquarium.getFishList().get(1).getHungerLevel());
        assertEquals(GROWTH_TIMER_START_VALUE, testAquarium.getFishList().get(1).getGrowthTimer());
        assertEquals(FISH_MIN_SIZE, testAquarium.getFishList().get(1).getSize());
        assertEquals(DEAD, testAquarium.getFishList().get(1).getStatus());
    }

    @Test
    public void testPassTimeManyFishWithStarveAndGrowth() {
        // Creates fish and adds to aquarium
        Fish fish1 = null;
        try {
            fish1 = new Fish(Fish.FISH_MIN_SIZE, FISH_HUNGER_TO_STARVE - 1,
                    Fish.ALIVE, Fish.GROWTH_TIMER_START_VALUE, null);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        Fish fish2 = null;
        try {
            fish2 = new Fish(Fish.FISH_MIN_SIZE, Fish.FISH_INITIAL_HUNGER_VALUE,
                    Fish.DEAD, Fish.GROWTH_TIMER_START_VALUE, null);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        Fish fish3 = null;
        try {
            fish3 = new Fish(Fish.FISH_MIN_SIZE, Fish.FISH_INITIAL_HUNGER_VALUE,
                    ALIVE, GROWTH_TIMER_END_VALUE + 1, null);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        testAquarium.addFish(fish1);
        testAquarium.addFish(fish2);
        testAquarium.addFish(fish3);

        // pass time
        testAquarium.passTime();

        // Check aquarium cleanness and size
        assertEquals(MAX_CLEANNESS_LEVEL - 1, testAquarium.getCleanness());
        assertEquals(3, testAquarium.getFishList().size());
        // Fish fish, with starve
        assertEquals(FISH_HUNGER_TO_STARVE, testAquarium.getFishList().get(0).getHungerLevel());
        assertEquals(GROWTH_TIMER_START_VALUE - 1, testAquarium.getFishList().get(0).getGrowthTimer());
        assertEquals(FISH_MIN_SIZE, testAquarium.getFishList().get(0).getSize());
        assertEquals(DEAD, testAquarium.getFishList().get(0).getStatus());
        // Second fish, no change due status
        assertEquals(FISH_INITIAL_HUNGER_VALUE, testAquarium.getFishList().get(1).getHungerLevel());
        assertEquals(GROWTH_TIMER_START_VALUE, testAquarium.getFishList().get(1).getGrowthTimer());
        assertEquals(FISH_MIN_SIZE, testAquarium.getFishList().get(1).getSize());
        assertEquals(DEAD, testAquarium.getFishList().get(1).getStatus());
        // third fish, with growth
        assertEquals(FISH_INITIAL_HUNGER_VALUE + 1, testAquarium.getFishList().get(2).getHungerLevel());
        assertEquals(GROWTH_TIMER_START_VALUE, testAquarium.getFishList().get(2).getGrowthTimer());
        assertEquals(FISH_MIN_SIZE + 1, testAquarium.getFishList().get(2).getSize());
        assertEquals(ALIVE, testAquarium.getFishList().get(2).getStatus());
    }

    @Test
    public void testFeedAllFishOne() {
        // add alive fish
        Fish fish = null;
        try {
            fish = new Fish(Fish.FISH_MIN_SIZE, Fish.FISH_INITIAL_HUNGER_VALUE,
                    Fish.ALIVE, Fish.GROWTH_TIMER_START_VALUE, null);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        testAquarium.addFish(fish);

        //feed
        testAquarium.feedAllFish();

        // test is fed
        assertEquals(1, testAquarium.getFishList().size());
        assertEquals(Math.max(FISH_INITIAL_HUNGER_VALUE - FISH_FOOD_AMOUNT, FISH_MIN_HUNGER),
                testAquarium.getFishList().get(0).getHungerLevel());
    }

    @Test
    public void testFeedAllFishDEAD() {
        // add dead fish
        Fish fish = null;
        try {
            fish = new Fish(Fish.FISH_MIN_SIZE, Fish.FISH_INITIAL_HUNGER_VALUE,
                    Fish.DEAD, Fish.GROWTH_TIMER_START_VALUE, null);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        testAquarium.addFish(fish);

        // feed
        testAquarium.feedAllFish();

        // test no fish has been fed
        assertEquals(1, testAquarium.getFishList().size());
        assertEquals(FISH_INITIAL_HUNGER_VALUE, testAquarium.getFishList().get(0).getHungerLevel());
    }

    @Test
    public void testFeedAllFishMany() {
        // adds two alive and one dead fish
        Fish fish1 = null;
        try {
            fish1 = new Fish(Fish.FISH_MIN_SIZE, Fish.FISH_INITIAL_HUNGER_VALUE,
                    Fish.ALIVE, Fish.GROWTH_TIMER_START_VALUE, null);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        Fish fish3 = null;
        try {
            fish3 = new Fish(Fish.FISH_MIN_SIZE, FISH_MIN_HUNGER,
                    Fish.ALIVE, Fish.GROWTH_TIMER_START_VALUE, null);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        Fish fish2 = null;
        try {
            fish2 = new Fish(Fish.FISH_MIN_SIZE, Fish.FISH_INITIAL_HUNGER_VALUE,
                    Fish.DEAD, Fish.GROWTH_TIMER_START_VALUE, null);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        testAquarium.addFish(fish1);
        testAquarium.addFish(fish2);
        testAquarium.addFish(fish3);

        // feed
        testAquarium.feedAllFish();

        // test all alive fish are fed and that hunger does not go below minimum hunger level
        assertEquals(3, testAquarium.getFishList().size());
        assertEquals(Math.max(FISH_INITIAL_HUNGER_VALUE - FISH_FOOD_AMOUNT, FISH_MIN_HUNGER),
                testAquarium.getFishList().get(0).getHungerLevel());
        assertEquals(Math.max(FISH_INITIAL_HUNGER_VALUE, FISH_MIN_HUNGER),
                testAquarium.getFishList().get(1).getHungerLevel());
        assertEquals(FISH_MIN_HUNGER, testAquarium.getFishList().get(2).getHungerLevel());
    }

    @Test
    public void testIsFishListEmptyTrue() {
        assertTrue(testAquarium.isFishListEmpty());
    }

    @Test
    public void testIsFishListEmptyFalse() {
        try {
            testAquarium.addFish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        assertFalse(testAquarium.isFishListEmpty());
    }

    @Test
    public void testIsAnyFishAliveNoFish() {
        assertFalse(testAquarium.isAnyFishAlive());
        assertEquals(0, testAquarium.getFishList().size());
    }

    @Test
    public void testIsAnyFishAliveTrue() {
        // add alive fish
        try {
            testAquarium.addFish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }

        // test that there is alive fish
        assertTrue(testAquarium.isAnyFishAlive());
        assertEquals(1, testAquarium.getFishList().size());
    }

    @Test
    public void testIsAnyFishAliveFalse() {
        // add dead
        Fish testFish = null;
        try {
            testFish = new Fish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        testFish.die();
        testAquarium.addFish(testFish);

        // test that there are no alive fish
        assertFalse(testAquarium.isAnyFishAlive());
        assertEquals(1, testAquarium.getFishList().size());
    }

    @Test
    public void testIsAnyFishAliveTrueWithDeadFish() {
        // add dead and alive fish
        Fish testFish = null;
        try {
            testFish = new Fish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        testFish.die();
        Fish testFish2 = null;
        try {
            testFish2 = new Fish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        testFish.die();
        testAquarium.addFish(testFish);
        try {
            testAquarium.addFish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        testAquarium.addFish(testFish2);

        // test that there are alive fish
        assertTrue(testAquarium.isAnyFishAlive());
        assertEquals(3, testAquarium.getFishList().size());

    }

    @Test
    public void testIsAnyFishDeadNoFish() {
        assertFalse(testAquarium.isAnyFishDead());
        assertEquals(0, testAquarium.getFishList().size());

    }

    @Test
    public void testIsAnyFishDeadFalse() {
        // add alive fish
        try {
            testAquarium.addFish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }

        // test that there is no dead fish
        assertFalse(testAquarium.isAnyFishDead());
        assertEquals(1, testAquarium.getFishList().size());

    }

    @Test
    public void testIsAnyFishDeadTrue() {
        // add dead
        Fish testFish = null;
        try {
            testFish = new Fish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        testFish.die();
        testAquarium.addFish(testFish);

        // test that there are is dead fish
        assertTrue(testAquarium.isAnyFishDead());
        assertEquals(1, testAquarium.getFishList().size());

    }

    @Test
    public void testIsAnyFishDeadTrueWithAliveFish() {
        // add dead and alive fish
        Fish testFish = null;
        try {
            testFish = new Fish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        testFish.die();
        try {
            testAquarium.addFish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        testAquarium.addFish(testFish);
        try {
            testAquarium.addFish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }

        // test that there is dead fish
        assertTrue(testAquarium.isAnyFishAlive());
        assertEquals(3, testAquarium.getFishList().size());
    }

    @Test
    public void testCleanNoDeadFish() {
        // make aquarium dirty, then test if dirty
        testAquarium.passTime();
        assertNotEquals(MAX_CLEANNESS_LEVEL, testAquarium.getCleanness());

        // clean, then test if clean
        testAquarium.clean();
        assertEquals(MAX_CLEANNESS_LEVEL, testAquarium.getCleanness());
    }

    @Test
    public void testCleanAlreadyClean() {
        // clean already clean aquarium
        testAquarium.clean();
        assertEquals(MAX_CLEANNESS_LEVEL, testAquarium.getCleanness());
    }

    @Test
    public void testGetLastFish() {
        Fish fishBob = null;
        try {
            fishBob = new Fish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        fishBob.setName("Bob");
        try {
            testAquarium.addFish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        testAquarium.addFish(fishBob);

        assertEquals(fishBob, testAquarium.getLastFish());
        assertEquals("Bob", testAquarium.getLastFish().getName());
    }


    @Test
    public void testCleanWithFish() {
        // Not clean aquarium, no fish
        testAquarium.passTime();
        assertNotEquals(MAX_CLEANNESS_LEVEL, testAquarium.getCleanness());
        assertEquals(0, testAquarium.getFishList().size());


        // add fish then clean
        try {
            testAquarium.addFish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        testAquarium.clean();

        // test fish still present and clean
        assertEquals(MAX_CLEANNESS_LEVEL, testAquarium.getCleanness());
        assertEquals(1, testAquarium.getFishList().size());
    }

    @Test
    public void testCleanDeadFish() {
        // Add two dead and one alive fish
        Fish ridFish1 = null;
        try {
            ridFish1 = new Fish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        ridFish1.die();
        Fish ridFish2 = null;
        try {
            ridFish2 = new Fish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        ridFish2.die();
        Fish keepFish = null;
        try {
            keepFish = new Fish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }

        //add fish to aquarium
        testAquarium.addFish(ridFish1);
        testAquarium.addFish(keepFish);
        testAquarium.addFish(ridFish2);

        // clean
        testAquarium.clean();

        // test if alive fish is still present
        assertEquals(MAX_CLEANNESS_LEVEL, testAquarium.getCleanness());
        assertEquals(1, testAquarium.getFishList().size());
        assertTrue(testAquarium.getFishList().contains(keepFish));
    }

    @Test
    public void testToJsonGeneralAquarium() {
        Fish aliveFish = null;
        try {
            aliveFish = new Fish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        Fish deadFish = null;
        try {
            deadFish = new Fish(2, 15, DEAD, 10, "Gone");
        } catch (IllegalFishException e) {
            fail("Unexpected exception");

        }
        testAquarium.addFish(aliveFish);
        testAquarium.addFish(deadFish);
        JSONObject jsonAquarium = testAquarium.toJson();
        JSONArray jsonFishList = jsonAquarium.getJSONArray("fishList");

        // test to check aquarium
        assertEquals(MAX_CLEANNESS_LEVEL, jsonAquarium.getInt("cleanness"));
        assertEquals(2, jsonAquarium.getJSONArray("fishList").length());

        // test to check aliveFish
        assertEquals(JSONObject.NULL,jsonFishList.getJSONObject(0).get("name"));
        assertEquals(FISH_INITIAL_HUNGER_VALUE, jsonFishList.getJSONObject(0).getInt("hungerLevel"));
        assertEquals(GROWTH_TIMER_START_VALUE, jsonFishList.getJSONObject(0).getInt("growthTimer"));
        assertEquals(FISH_MIN_SIZE, jsonFishList.getJSONObject(0).getInt("size"));
        assertEquals(ALIVE, jsonFishList.getJSONObject(0).getInt("status"));

        // test to check deadFish
        assertEquals("Gone",jsonFishList.getJSONObject(1).getString("name"));
        assertEquals(15, jsonFishList.getJSONObject(1).getInt("hungerLevel"));
        assertEquals(10, jsonFishList.getJSONObject(1).getInt("growthTimer"));
        assertEquals(2, jsonFishList.getJSONObject(1).getInt("size"));
        assertEquals(DEAD, jsonFishList.getJSONObject(1).getInt("status"));
    }

}

