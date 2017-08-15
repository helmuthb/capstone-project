package at.breitenfellner.roomquestions.model;

import com.google.firebase.database.Exclude;

/**
 * Data class for a question.
 */

public class Question {
    public String text;
    public String author;
    public String owner;
    public long date;
    public String key;
}
