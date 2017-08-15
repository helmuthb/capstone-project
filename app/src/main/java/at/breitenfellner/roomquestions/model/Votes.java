package at.breitenfellner.roomquestions.model;

/**
 * Data class managing votes of a question.
 */

public class Votes {
    public Question question;
    public boolean votedByMy;
    public int voteCount;

    public Votes(Question question, boolean votedByMy, int voteCount) {
        this.question = question;
        this.votedByMy = votedByMy;
        this.voteCount = voteCount;
    }
}
