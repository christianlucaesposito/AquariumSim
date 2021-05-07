# Aquarisim

## An aquarium simulator

What will this app do? Why? For Who?

This application will simulate an aquarium, where users can feed fish and take care of them.
It is meant for anyone looking for a small hobby, or time passer! It is meant for entertainment.
I am interested in this project as it seems something fun to do! I enjoy video-games and fish,
so I thought to combine both and make this application. 

## User Stories
NOTE: The passtime button is to for demo! It allows passage of time to go faster :)

Things I want to be able to do as a **user**:
- As a user, I want to be able to add new fish to my aquarium
- As a user, I want to be able to feed my fish
- As a user, I want to be able to know about my fish (hunger level, 
whether it is dead or alive, size and if tank is dirty)
- As a user, I want to be able to clean my aquarium and remove any dead fish 
(*uhh...* ~~_this sounds like a chore..._~~)
- As a user, I want to be able to name my fish. 
- As a user, I want to be able to make time pass, and allow my fish to get hungry/grow/die,
and my tank to get dirty.
- As a user, I want to be able to save the status of my aquarium
- As a user, I want to be able to give option to load a save file.
- As a user, I want to hear relaxing music

*Stretch goals*:
- As a user, I want different fish types
- As a user, I want each fish to be unique (own hunger, color, etc.)
- As a user, I want to experience background passage of time **(COMPLETE!)**
- As a user, I want to see my fish swim around **(COMPLETE!)**
- As a user, I want to add Aquarium decorations
- As a user, I want to rename my fish
- As a user, I want to hear button sounds
- As a user, I want to be able to mute sounds

## Update notes
I made classes more robust by adding exceptions (IllegalFishException, IllegalAquariumException,
IllegalDirectionException and OutOfBoundException). For both IllegalFishException and IllegalAquariumException 
exceptions (used in constructor) it allowed me to identify when there is a corruption of data in persistence when 
rebuilding the fish and/or aquarium. These exceptions are passed on until caught in the GUI.
I tested these exceptions in the model tests and the JsonReaderTest.
Ultimately, this allowed me to add alert messages about corruption data in the GUI!

Instead, IllegalDirection is used when setting direction for fish (Right or Left, 0 or 1). This allowed me to make
methods more robust and make sure there is no accidental errors! OutOfBoundsException instead setting coordinates x and 
y. This prevents from setting values outside of bounds.

##### Classes and methods in that play a role in this task:
Aquarium (Class)
- Aquarium(int cleanness)
- addFish()
- addFish(int size, int initialHunger, int status, int growthTimer)

Fish (Class)
- Fish()
- Fish(int size, int hungerLevel, int status, int growthTimer, String name) 
- setCoordinateY(double y)
- setCoordinateX(double x)
- setDirection(int direction)

JsonReader (Class)
- read()
- parseAquarium(JSONObject jsonObject)
- addFishList(Aquarium aquarium, JSONObject jsonObject)
- addFish(Aquarium aquarium, JSONObject jsonObject)

## Update Notes
NOTE: UML does not contain TextBasedCommandUI as it is an unused UI, that was initially created to show my project in
phase 1. 

My UML show that too many classes associations with Aquarium, even the GUI. I Would refactor so that there aren't as 
many associations especially when GUI is also dependent on the classes that depend on the aquarium (even if it also 
depends on aquarium itself). There is also a bidirectional association between ButtonCommand and GUI, which seems to 
create coupling. If I had more time I would try to approach it differently. As well as I would separate the sound and
music methods from GUI into its own class.

I would create a superclass that contains an Aquarium any other information that UI needs to access in general,
I would extend it in all the UI components, so they would have easy access to common states in model package.

## Sources
- Implementation of persistence is based on JsonSerializationDemo, from CPSC 210 UBC.
- Fish art from https://mastodon.art/@RAPIDPUNCHES/103569305236570185
- Music from https://www.scottbuckley.com.au/library/wp-content/uploads/2020/03/sb_reverie.mp3
