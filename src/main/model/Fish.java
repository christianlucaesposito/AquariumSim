package model;

import exception.IllegalDirectionException;
import exception.IllegalFishException;
import exception.OutOfBoundsException;
import org.json.JSONObject;
import persistence.Writable;

/*
 * Represents a fish having hunger level, size , name, a growth timer and it's status;
 * hunger level has a default initial value, minimum value and a value for when fish starved and dies;
 * growth timer is a timer until fish grows of size;
 * status is whether the fish is dead or alive;
 * fish has an initial minimum size, and a maximum size;
 * fish has a random swim speed between MIN_SPEED and MAX_SPEED
 * Contains coordinate x and y, and direction it is faction
 */
public class Fish implements Writable {
    public static final int FISH_FOOD_AMOUNT = 5;
    public static final int FISH_HUNGER_TO_STARVE = 15;
    public static final int FISH_MIN_HUNGER = 0;
    public static final int GROWTH_TIMER_START_VALUE = 50;
    public static final int GROWTH_TIMER_END_VALUE = 0;
    public static final int FISH_MIN_SIZE = 1;
    public static final int FISH_MAX_SIZE = 20;
    public static final int FISH_INITIAL_HUNGER_VALUE = 2;
    public static final int DEAD = 0;
    public static final int ALIVE = 1;
    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    public static final double MIN_SPEED = 0.001;
    public static final double MAX_SPEED = 0.010;


    private double speed;
    private int status;
    private int hungerLevel;
    private int growthTimer;
    private int size;
    private String name;
    private double coordinateX;
    private double coordinateY;
    private int direction;

    // EFFECTS: constructs new fish; with hungerLevel set to 5; size of 1; status alive, and growthTimer set to 50;
    // throws exception illegal fish if parameters not valid
    public Fish() throws IllegalFishException {
        this(FISH_MIN_SIZE, FISH_INITIAL_HUNGER_VALUE, ALIVE, GROWTH_TIMER_START_VALUE, null);
    }

    // EFFECTS: constructs new fish with specified size, hungerLevel, status, growthTimer
    // with a randomized coordinate x and y, and direction (left or right), and a random speed
    // throws exception illegal fish if parameters not valid
    public Fish(int size, int hungerLevel, int status, int growthTimer, String name) throws IllegalFishException {
        if (size < FISH_MIN_SIZE || hungerLevel < FISH_MIN_HUNGER || status > ALIVE
                || status < DEAD || growthTimer < GROWTH_TIMER_END_VALUE) {
            throw new IllegalFishException();
        } else {
            this.hungerLevel = hungerLevel;
            this.size = size;
            this.status = status;
            this.growthTimer = growthTimer;
            this.name = name;
            this.coordinateX = Math.random();
            this.coordinateY = Math.random() * 0.95;
            this.direction = Math.random() > 0.5 ? LEFT : RIGHT;
            randomizeSwimSpeed();
        }
    }

    // EFFECTS: return x coordinate
    public double getCoordinateX() {
        return coordinateX;
    }

    // EFFECTS: return swim direction
    public int getDirection() {
        return direction;
    }

    // EFFECTS: return y coordinate
    public double getCoordinateY() {
        return coordinateY;
    }

    // EFFECTS: return fish speed
    public double getSpeed() {
        return speed;
    }

    // MODIFIES: this
    // EFFECTS: sets y as coordinate coordinate
    // throws OutOfBoundsException if coordinate are out of bounds
    public void setCoordinateY(double y) throws OutOfBoundsException {
        if (y < 0 || y > 1) {
            throw new OutOfBoundsException();
        } else {
            coordinateY = y;
        }
    }

    // MODIFIES: this
    // EFFECTS: sets y as coordinate coordinate
    // throws OutOfBoundsException if coordinate are out of bounds
    public void setCoordinateX(double x) throws OutOfBoundsException {
        if (x < 0.0 || x > 1.0) {
            throw new OutOfBoundsException();
        } else {
            coordinateX = x;
        }
    }

    // REQUIRES: direction = RIGHT (0) || LEFT (1)
    // MODIFIES: this
    // EFFECT: sets direction as fish direction
    // throws IllegalDirectionException if direction value not valid
    public void setDirection(int direction) throws IllegalDirectionException {
        if (direction != RIGHT && direction != LEFT) {
            throw new IllegalDirectionException();
        } else {
            this.direction = direction;
        }
    }

    // MODIFIES: this
    // EFFECTS: If fish is dead then adds double the speed to coordinate Y
    // Else adds speed to coordinate X if direction is right, else subtracts speed
    // if coordinate X is bigger than 1 then change direction of fish to left and calls randomizeSwimSpeed
    // if coordinate X is smaller than 0 then changes direction of fish to right and calls randomizeSwimSpeed
    public void updateCoordinate() {
        if (status == DEAD) {
            coordinateY += speed * 2.0;
            if (coordinateY > 0.9) {
                coordinateY = 0.9;
            }
        } else {
            coordinateX += direction == RIGHT ? speed : -speed;
            if (coordinateX > 1.0) {
                coordinateX = 1.0;
                direction = LEFT;
                randomizeSwimSpeed();
            }
            if (coordinateX < 0.0) {
                coordinateX = 0.0;
                direction = RIGHT;
                randomizeSwimSpeed();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: randomize speed swim speed of fish, not more than max speed
    private void randomizeSwimSpeed() {
        speed = MIN_SPEED + Math.random() * (MAX_SPEED - MIN_SPEED);
    }

    // EFFECTS: return hunger level
    public int getHungerLevel() {
        return this.hungerLevel;
    }

    // EFFECTS: return fish size
    public int getSize() {
        return this.size;
    }

    // EFFECTS: return fish growth timer
    public int getGrowthTimer() {
        return this.growthTimer;
    }

    // EFFECTS: return fish name
    public String getName() {
        return this.name;
    }

    // EFFECTS: return fish status (0 = Dead, 1 = Alive)
    public int getStatus() {
        return this.status;
    }

    @Override
    // EFFECTS: Prints fish name, hunger, and its status (dead or alive), and its size
    public String toString() {
        return "Fish name=" + name + " hunger=" + hungerLevel + " size=" + size + " state="
                + (status == Fish.ALIVE ? "alive" : "dead");
    }


    // REQUIRES: hungerLevel < FISH_HUNGER_TO_STARVE.
    // MODIFIES: this.
    // EFFECTS: Reduces hunger level of fish by 5 points to a minimum of 0 only if alive.
    public void feed() {
        if (this.status == ALIVE) {
            this.hungerLevel -= FISH_FOOD_AMOUNT;

            if (hungerLevel < FISH_MIN_HUNGER) {
                hungerLevel = FISH_MIN_HUNGER;
            }
        }
    }

    // MODIFIES: this.
    // EFFECTS: Names fish, or renames the fish if it already has a name.
    public void setName(String name) {
        this.name = name;
    }

    // MODIFIES: this.
    // EFFECTS: sets fish alive status to dead
    public void die() {
        this.status = DEAD;
    }

    // MODIFIES: this.
    // EFFECTS: sets fish alive status to dead if fish hunger reaches FISH_HUNGER_TO_STARVE
    public void starveCheck() {
        if (this.hungerLevel >= FISH_HUNGER_TO_STARVE) {
            this.die();
        }
    }

    // MODIFIES: this
    // EFFECTS: If growth timer is zero increases fish size by 1, to a maximum of 20 and resets timer
    public void growCheck() {
        if (this.status == ALIVE && growthTimer == GROWTH_TIMER_END_VALUE) {
            this.size = Math.min(size + 1, FISH_MAX_SIZE);
            this.growthTimer = GROWTH_TIMER_START_VALUE;
        }
    }

    // MODIFIES: this
    // EFFECTS: If fish is alive, it increases hunger by 1, and decreases growth timer by 1,
    // checks if fish has starved, if so sets status to dead, else remains alive.
    public void passTime() {
        if (this.status == ALIVE) {
            this.hungerLevel++;
            this.growthTimer--;

            starveCheck();
            growCheck();
        }
    }

    // EFFECTS: Returns hunger level as a string
    public String hungerToString() {
        if (hungerLevel == FISH_MIN_HUNGER) {
            return "Stuffed";
        } else if (hungerLevel <= FISH_HUNGER_TO_STARVE / 3) {
            return  "Not Hungry";
        }  else  if (hungerLevel >= FISH_HUNGER_TO_STARVE - 1) {
            return "Starving";
        } else {
            return "Hungry";
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name == null ? JSONObject.NULL : name);
        json.put("status", status);
        json.put("hungerLevel", hungerLevel);
        json.put("growthTimer", growthTimer);
        json.put("size", size);
        return json;
    }
}
