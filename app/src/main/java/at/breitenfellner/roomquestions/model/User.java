package at.breitenfellner.roomquestions.model;

/**
 * Data class for a user
 */

public class User {
    public String name;
    public android.net.Uri photoUrl;
    public String uid;

    public User(String name, android.net.Uri photoUrl, String uid) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.uid = uid;
    }
}
