package at.breitenfellner.roomquestions.model;

/**
 * Data class for a room.
 */

public class Room {
    public String name;
    public String description;
    public String key;

    public Room() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }
}
