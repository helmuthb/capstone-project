package at.breitenfellner.roomquestions.model;

/**
 * Data class for a user
 */

public class User {
    public String name;
    public android.net.Uri photoUrl;

    public User(String name, android.net.Uri photoUrl) {
        this.name = name;
        this.photoUrl = photoUrl;
    }
}
