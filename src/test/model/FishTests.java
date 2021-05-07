package model;

import static model.Fish.*;
import static org.junit.jupiter.api.Assertions.*;

import exception.IllegalDirectionException;
import exception.IllegalFishException;
import exception.OutOfBoundsException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * Tests for Fish Class
 */
public class FishTests {
    private Fish testFish;
    private static final int FISH_MAX_HUNGER = FISH_HUNGER_TO_STARVE - 1;

    @BeforeEach
    public void setup() {
        try {
            testFish = new Fish();
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testFishConstructorExceptionSize() {
        try {
            testFish = new Fish(FISH_MIN_SIZE - 1,
                    FISH_INITIAL_HUNGER_VALUE, ALIVE, GROWTH_TIMER_START_VALUE, null);
            fail("Uncaught exception");
        } catch (IllegalFishException e) {
            // expected
        }
    }

    @Test
    public void testFishConstructorExceptionHunger() {
        try {
            testFish = new Fish(FISH_MIN_SIZE , FISH_MIN_HUNGER - 1 ,
                    ALIVE, GROWTH_TIMER_START_VALUE, null);
            fail("Uncaught exception");
        } catch (IllegalFishException e) {
            // expected
        }
    }

    @Test
    public void testFishConstructorExceptionStatus() {
        try {
            testFish = new Fish(FISH_MIN_SIZE, FISH_INITIAL_HUNGER_VALUE, 3,
                    GROWTH_TIMER_START_VALUE, null);
            fail("Uncaught exception");
        } catch (IllegalFishException e) {
            // expected
        }
    }

    @Test
    public void testFishConstructorExceptionStatusLow() {
        try {
            testFish = new Fish(FISH_MIN_SIZE, FISH_INITIAL_HUNGER_VALUE, -1,
                    GROWTH_TIMER_START_VALUE, null);
            fail("Uncaught exception");
        } catch (IllegalFishException e) {
            // expected
        }
    }

    @Test
    public void testFishConstructorExceptionGrowth() {
        try {
            testFish = new Fish(FISH_MIN_SIZE, FISH_INITIAL_HUNGER_VALUE, ALIVE,
                    GROWTH_TIMER_END_VALUE - 1, null);
            fail("Uncaught exception");
        } catch (IllegalFishException e) {
            // expected
        }
    }

    @Test
    public void testFishConstructorSpecified() {
        try {
            testFish = new Fish(3, 5, ALIVE, 1, "Penny");
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        assertEquals(this.testFish.getHungerLevel(), 5);
        assertEquals(this.testFish.getStatus(), ALIVE);
        assertEquals(this.testFish.getSize(), 3);
        assertEquals(this.testFish.getGrowthTimer(), 1);
        assertEquals(this.testFish.getName(), "Penny");
    }

    @Test
    public void testSetCoordinateY() {
        try {
            testFish.setCoordinateY(0.3);
        } catch (OutOfBoundsException e) {
            fail("unexpected exception");
        }
    }

    @Test
    public void testSetCoordinateYExceptionOutOfBoundsLeft() {
        try {
            testFish.setCoordinateY(-1);
            fail("expected exception missing");
        } catch (OutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testSetCoordinateYExceptionOutOfBoundsRight() {
        try {
            testFish.setCoordinateY(2);
            fail("unexpected exception");
        } catch (OutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testSetCoordinateX() {
        try {
            testFish.setCoordinateX(0.3);
        } catch (OutOfBoundsException e) {
            fail("unexpected exception");
        }
    }

    @Test
    public void testSetCoordinateXExceptionOutOfBoundsLeft() {
        try {
            testFish.setCoordinateX(-1);
            fail("expected exception missing");
        } catch (OutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testSetCoordinateXExceptionOutOfBoundsRight() {
        try {
            testFish.setCoordinateX(2);
            fail("unexpected exception");
        } catch (OutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testSetDirectionRight() {
        try {
            testFish.setDirection(RIGHT);
        } catch (IllegalDirectionException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testSetDirectionLeft() {
        try {
            testFish.setDirection(LEFT);
        } catch (IllegalDirectionException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testSetDirectionIllegalDirectionException() {
        try {
            testFish.setDirection(5);
            fail("expected exception not thrown");
        } catch (IllegalDirectionException e) {
            // expected
        }
    }

    @Test
    public void testFishConstructor() {
        assertEquals(this.testFish.getHungerLevel(), FISH_INITIAL_HUNGER_VALUE);
        assertEquals(this.testFish.getStatus(), ALIVE);
        assertEquals(this.testFish.getSize(), FISH_MIN_SIZE);
        assertEquals(this.testFish.getGrowthTimer(), GROWTH_TIMER_START_VALUE);
        assertNull(this.testFish.getName());
        assertTrue(testFish.getCoordinateX() <= 1);
        assertTrue(testFish.getCoordinateX() >= 0);
        assertTrue(testFish.getCoordinateY() <= 1);
        assertTrue(testFish.getCoordinateY() >= 0);
        assertTrue(testFish.getDirection() == RIGHT || testFish.getDirection() == LEFT);
    }

    @Test
    public void testToString() {
        assertEquals("Fish name=null hunger=2 size=1 state=alive", testFish.toString());
    }

    @Test
    public void testToStringDeadFish() {
        testFish.die();
        assertEquals("Fish name=null hunger=2 size=1 state=dead", testFish.toString());
    }

    @Test
    public void testUpdateCoordinateDeadFalling() {
        testFish.die();
        try {
            testFish.setCoordinateY(0.1);
        } catch (OutOfBoundsException e) {
            fail("Unexpected exception");
        }
        double cordY = testFish.getCoordinateY() + testFish.getSpeed() * 2;
        double oldCordX = testFish.getCoordinateX();
        double oldSpeed = testFish.getSpeed();

        testFish.updateCoordinate();

        assertEquals(cordY, testFish.getCoordinateY());
        assertEquals(oldCordX, testFish.getCoordinateX());
        assertEquals(oldSpeed, testFish.getSpeed());
    }

    @Test
    public void testUpdateCoordinateDeadOutBounds() {
        testFish.die();
        try {
            testFish.setCoordinateY(1);
        } catch (OutOfBoundsException e) {
            fail("Unexpected exception");
        }
        double oldCordX = testFish.getCoordinateX();

        testFish.updateCoordinate();

        assertEquals(0.9, testFish.getCoordinateY());
        assertEquals(oldCordX, testFish.getCoordinateX());
    }

    @Test
    public void testUpdateCoordinateDeadReachedBottom() {
        testFish.die();
        try {
            testFish.setCoordinateY(0.9);
        } catch (OutOfBoundsException e) {
            fail("Unexpected exception");
        }

        double oldCordX = testFish.getCoordinateX();
        double oldSpeed = testFish.getSpeed();

        testFish.updateCoordinate();

        assertEquals(0.9, testFish.getCoordinateY());
        assertEquals(oldCordX, testFish.getCoordinateX());
        assertEquals(oldSpeed, testFish.getSpeed());
    }

    @Test
    public void testUpdateCoordinateRight() {
        try {
            testFish.setCoordinateX(0.0);
        } catch (OutOfBoundsException e) {
            fail("Unexpected exception");
        }
        try {
            testFish.setDirection(RIGHT);
        } catch (IllegalDirectionException e) {
            fail("Unexpected exception");
        }
        double cordX = testFish.getCoordinateX() + testFish.getSpeed();
        double cordY = testFish.getCoordinateY();
        double oldSpeed = testFish.getSpeed();

        testFish.updateCoordinate();

        assertEquals(cordY, testFish.getCoordinateY());
        assertEquals(cordX, testFish.getCoordinateX());
        assertEquals(RIGHT, testFish.getDirection());
        assertEquals(oldSpeed, testFish.getSpeed());
    }

    @Test
    public void testUpdateCoordinateRightOutBounds() {
        try {
            testFish.setCoordinateX(1.0);
        } catch (OutOfBoundsException e) {
            fail("Unexpected exception");
        }
        try {
            testFish.setDirection(RIGHT);
        } catch (IllegalDirectionException e) {
            fail("Unexpected exception");
        }
        double cordY = testFish.getCoordinateY();


        testFish.updateCoordinate();

        assertEquals(cordY, testFish.getCoordinateY());
        assertEquals(1.0, testFish.getCoordinateX());
        assertEquals(LEFT, testFish.getDirection());
        assertTrue(testFish.getSpeed() >= MIN_SPEED);
        assertTrue(testFish.getSpeed() <= MAX_SPEED);
    }

    @Test
    public void testUpdateCoordinateLeft() {
        try {
            testFish.setCoordinateX(1.0);
        } catch (OutOfBoundsException e) {
            fail("Unexpected exception");
        }
        try {
            testFish.setDirection(LEFT);
        } catch (IllegalDirectionException e) {
            fail("Unexpected exception");
        }
        double cordY = testFish.getCoordinateY();
        double cordX = testFish.getCoordinateX() - testFish.getSpeed();
        double oldSpeed = testFish.getSpeed();

        testFish.updateCoordinate();

        assertEquals(cordY, testFish.getCoordinateY());
        assertEquals(cordX, testFish.getCoordinateX());
        assertEquals(LEFT, testFish.getDirection());
        assertEquals(oldSpeed, testFish.getSpeed());
    }

    @Test
    public void testUpdateCoordinateLeftOutBounds() {
        try {
            testFish.setCoordinateX(0.0);
        } catch (OutOfBoundsException e) {
            fail("Unexpected exception");
        }
        try {
            testFish.setDirection(LEFT);
        } catch (IllegalDirectionException e) {
            fail("Unexpected exception");
        }
        double cordY = testFish.getCoordinateY();


        testFish.updateCoordinate();

        assertEquals(cordY, testFish.getCoordinateY());
        assertEquals(0.0, testFish.getCoordinateX());
        assertEquals(RIGHT, testFish.getDirection());
        assertTrue(testFish.getSpeed() >= MIN_SPEED);
        assertTrue(testFish.getSpeed() <= MAX_SPEED);
    }

    @Test
    public void testHungerToStringStuffed() {
        Fish stuffedFish = null;
        try {
            stuffedFish = new Fish(FISH_MIN_SIZE, FISH_MIN_HUNGER, ALIVE,
                    GROWTH_TIMER_START_VALUE, "Pedro");
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        assertEquals("Stuffed", stuffedFish.hungerToString());
    }

    @Test
    public void testHungerToStringNotHungry() {
        Fish stuffedFish = null;
        try {
            stuffedFish = new Fish(FISH_MIN_SIZE, FISH_HUNGER_TO_STARVE / 3, ALIVE,
                    GROWTH_TIMER_START_VALUE, "Pedro");
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        assertEquals("Not Hungry", stuffedFish.hungerToString());
    }

    @Test
    public void testHungerToStringNotHungryLess() {
        Fish stuffedFish = null;
        try {
            stuffedFish = new Fish(FISH_MIN_SIZE, FISH_HUNGER_TO_STARVE / 3 - 1, ALIVE,
                    GROWTH_TIMER_START_VALUE, "Pedro");
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        assertEquals("Not Hungry", stuffedFish.hungerToString());
    }

    @Test
    public void testHungerToStringHungry() {
        Fish stuffedFish = null;
        try {
            stuffedFish = new Fish(FISH_MIN_SIZE, FISH_HUNGER_TO_STARVE / 2, ALIVE,
                    GROWTH_TIMER_START_VALUE, "Pedro");
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        assertEquals("Hungry", stuffedFish.hungerToString());
    }

    @Test
    public void testHungerToStringStarving() {
        Fish stuffedFish = null;
        try {
            stuffedFish = new Fish(FISH_MIN_SIZE, FISH_HUNGER_TO_STARVE,
                    ALIVE,GROWTH_TIMER_START_VALUE, "Pedro");
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        assertEquals("Starving", stuffedFish.hungerToString());
    }

    @Test
    public void testHungerToStringStarvingYetTo() {
        Fish stuffedFish = null;
        try {
            stuffedFish = new Fish(FISH_MIN_SIZE, FISH_MAX_HUNGER, ALIVE,GROWTH_TIMER_START_VALUE, "Pedro");
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }
        assertEquals("Starving", stuffedFish.hungerToString());
    }

    @Test
    public void testFeedFishHungerAtFoodAmount() {
        try {
            testFish = new Fish(FISH_MIN_SIZE, FISH_MIN_HUNGER + FISH_FOOD_AMOUNT, ALIVE,
                    GROWTH_TIMER_START_VALUE, null);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }

        testFish.feed();
        assertEquals(FISH_MIN_HUNGER, this.testFish.getHungerLevel());
        assertEquals(this.testFish.getStatus(), ALIVE);
        assertEquals(this.testFish.getSize(), FISH_MIN_SIZE);
        assertEquals(this.testFish.getGrowthTimer(), GROWTH_TIMER_START_VALUE);
        assertNull(this.testFish.getName());
    }

    @Test
    public void testFeedFishMinimumHunger() {
        try {
            testFish = new Fish(FISH_MIN_SIZE, FISH_MIN_HUNGER, ALIVE,
                    GROWTH_TIMER_START_VALUE, null);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }

        testFish.feed();
        assertEquals(FISH_MIN_HUNGER, this.testFish.getHungerLevel());
        assertEquals(this.testFish.getStatus(), ALIVE);
        assertEquals(this.testFish.getSize(), FISH_MIN_SIZE);
        assertEquals(this.testFish.getGrowthTimer(), GROWTH_TIMER_START_VALUE);
        assertNull(this.testFish.getName());
    }

    @Test
    public void testFeedFishMaxHunger() {
        try {
            testFish = new Fish(FISH_MIN_SIZE, FISH_MAX_HUNGER, ALIVE, GROWTH_TIMER_START_VALUE, null);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }

        testFish.feed();
        assertEquals(FISH_MAX_HUNGER - FISH_FOOD_AMOUNT, this.testFish.getHungerLevel());
        assertEquals(this.testFish.getStatus(), ALIVE);
        assertEquals(this.testFish.getSize(), FISH_MIN_SIZE);
        assertEquals(this.testFish.getGrowthTimer(), GROWTH_TIMER_START_VALUE);
        assertNull(this.testFish.getName());
    }

    @Test
    public void testFeedFishDEAD() {
        try {
            testFish = new Fish(FISH_MIN_SIZE, FISH_MAX_HUNGER, DEAD, GROWTH_TIMER_START_VALUE, null);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }

        testFish.feed();
        assertEquals(FISH_MAX_HUNGER, this.testFish.getHungerLevel());
        assertEquals(this.testFish.getStatus(), DEAD);
        assertEquals(this.testFish.getSize(), FISH_MIN_SIZE);
        assertEquals(this.testFish.getGrowthTimer(), GROWTH_TIMER_START_VALUE);
        assertNull(this.testFish.getName());
    }

    @Test
    public void testNameFish() {
        this.testFish.setName("Penny");
        assertEquals("Penny", this.testFish.getName());
    }

    @Test
    public void testRenameFish() {
        this.testFish.setName("Penny");
        assertEquals("Penny", this.testFish.getName());

        this.testFish.setName("Bob");
        assertEquals("Bob", this.testFish.getName());
    }

    @Test
    public void testDie() {
        this.testFish.die();
        assertEquals(DEAD, this.testFish.getStatus());
    }

    // GROWTH_TIMER_START_VALUE must be at least 2
    @Test
    public void testGrowCheckNoGrowth() {
        this.testFish.die();
        this.testFish.growCheck();

        assertEquals(FISH_INITIAL_HUNGER_VALUE, this.testFish.getHungerLevel());
        assertEquals(GROWTH_TIMER_START_VALUE, this.testFish.getGrowthTimer());
        assertEquals(DEAD, this.testFish.getStatus());
        assertEquals(FISH_MIN_SIZE, this.testFish.getSize());
    }

    @Test
    public void testGrowCheckNoGrowthAndDead() {
        this.testFish.growCheck();
        assertEquals(FISH_INITIAL_HUNGER_VALUE, this.testFish.getHungerLevel());
        assertEquals(GROWTH_TIMER_START_VALUE, this.testFish.getGrowthTimer());
        assertEquals(ALIVE, this.testFish.getStatus());
        assertEquals(FISH_MIN_SIZE, this.testFish.getSize());
    }

    @Test
    public void testGrowCheckWithGrowth() {
        try {
            testFish = new Fish(FISH_MIN_SIZE, FISH_INITIAL_HUNGER_VALUE, ALIVE, GROWTH_TIMER_END_VALUE, null);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }

        this.testFish.growCheck();
        assertEquals(FISH_INITIAL_HUNGER_VALUE, this.testFish.getHungerLevel());
        assertEquals(GROWTH_TIMER_START_VALUE, this.testFish.getGrowthTimer());
        assertEquals(ALIVE, this.testFish.getStatus());
        assertEquals(FISH_MIN_SIZE + 1, this.testFish.getSize());
    }

    @Test
    public void testGrowCheckNoGrowthDueDeath() {
        try {
            testFish = new Fish(FISH_MIN_SIZE, FISH_INITIAL_HUNGER_VALUE, DEAD, GROWTH_TIMER_END_VALUE, null);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }

        this.testFish.growCheck();
        assertEquals(FISH_INITIAL_HUNGER_VALUE, this.testFish.getHungerLevel());
        assertEquals(GROWTH_TIMER_END_VALUE, this.testFish.getGrowthTimer());
        assertEquals(DEAD, this.testFish.getStatus());
        assertEquals(FISH_MIN_SIZE, this.testFish.getSize());
    }

    @Test
    public void testPassTime() {
        this.testFish.passTime();
        assertEquals(FISH_INITIAL_HUNGER_VALUE + 1, this.testFish.getHungerLevel());
        assertEquals(GROWTH_TIMER_START_VALUE - 1, this.testFish.getGrowthTimer());
        assertEquals(ALIVE, this.testFish.getStatus());
        assertEquals(FISH_MIN_SIZE, this.testFish.getSize());
    }

    @Test
    public void testPassTimeDead() {
        this.testFish.die();

        this.testFish.passTime();
        assertEquals(FISH_INITIAL_HUNGER_VALUE, this.testFish.getHungerLevel());
        assertEquals(GROWTH_TIMER_START_VALUE, this.testFish.getGrowthTimer());
        assertEquals(DEAD, this.testFish.getStatus());
        assertEquals(FISH_MIN_SIZE, this.testFish.getSize());
    }

    @Test
    public void testPassTimeWithGrowth() {
        try {
            testFish = new Fish(FISH_MIN_SIZE, FISH_MIN_HUNGER, ALIVE, GROWTH_TIMER_END_VALUE + 1, null);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }

        testFish.passTime();
        assertEquals(FISH_MIN_HUNGER + 1, this.testFish.getHungerLevel());
        assertEquals(FISH_MIN_SIZE + 1, this.testFish.getSize());
        assertEquals(GROWTH_TIMER_START_VALUE, this.testFish.getGrowthTimer());
        assertEquals(ALIVE, this.testFish.getStatus());
    }

    @Test
    public void testPassTimeDie() {
        try {
            testFish =  new Fish(FISH_MIN_SIZE, FISH_MAX_HUNGER, ALIVE, GROWTH_TIMER_START_VALUE, null);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }

        testFish.passTime();
        assertEquals(FISH_HUNGER_TO_STARVE, this.testFish.getHungerLevel());
        assertEquals(GROWTH_TIMER_START_VALUE - 1, this.testFish.getGrowthTimer());
        assertEquals(DEAD, this.testFish.getStatus());
        assertEquals(FISH_MIN_SIZE, this.testFish.getSize());
    }

    @Test
    public void testPassTimeDieNoGrowth() {
        try {
            testFish =  new Fish(FISH_MIN_SIZE, FISH_MAX_HUNGER, ALIVE, GROWTH_TIMER_END_VALUE + 1, null);
        } catch (IllegalFishException e) {
            fail("Unexpected exception");
        }

        testFish.passTime();
        assertEquals(FISH_HUNGER_TO_STARVE, this.testFish.getHungerLevel());
        assertEquals(GROWTH_TIMER_END_VALUE, this.testFish.getGrowthTimer());
        assertEquals(DEAD, this.testFish.getStatus());
        assertEquals(FISH_MIN_SIZE, this.testFish.getSize());
    }

    @Test
    public void testSetName() {
        testFish.setName("Penny");
        assertEquals("Penny",testFish.getName());
    }

    @Test
    public void testToJsonGeneralFish(){
        JSONObject jsonFish = testFish.toJson();
        assertEquals(JSONObject.NULL,jsonFish.get("name"));
        assertEquals(FISH_INITIAL_HUNGER_VALUE, jsonFish.getInt("hungerLevel"));
        assertEquals(GROWTH_TIMER_START_VALUE, jsonFish.getInt("growthTimer"));
        assertEquals(FISH_MIN_SIZE, jsonFish.getInt("size"));
        assertEquals(ALIVE, jsonFish.getInt("status"));
    }

    @Test
    public void testToJsonEdgeFish(){
        testFish.passTime();
        testFish.die();
        testFish.setName("Potato");
        JSONObject jsonFish = testFish.toJson();
        assertEquals("Potato",jsonFish.get("name"));
        assertEquals(FISH_INITIAL_HUNGER_VALUE + 1, jsonFish.getInt("hungerLevel"));
        assertEquals(GROWTH_TIMER_START_VALUE - 1, jsonFish.getInt("growthTimer"));
        assertEquals(1, jsonFish.getInt("size"));
        assertEquals(DEAD, jsonFish.getInt("status"));
    }
}
