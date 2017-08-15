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

    public Question() {}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
