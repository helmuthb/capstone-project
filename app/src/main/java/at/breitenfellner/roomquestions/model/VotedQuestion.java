package at.breitenfellner.roomquestions.model;

/**
 * Data class managing voted questions.
 */

public class VotedQuestion extends Question {
    public boolean votedByMe;
    public int voteCount;

    public VotedQuestion(Question q) {
        this.key = q.key;
        this.author = q.author;
        this.date = q.date;
        this.owner = q.owner;
        this.text = q.text;
    }

    public VotedQuestion(Question q, boolean votedByMe, int voteCount) {
        this(q);
        this.votedByMe = votedByMe;
        this.voteCount = voteCount;
    }

    public static class Comparator implements java.util.Comparator<VotedQuestion> {
        @Override
        public int compare(VotedQuestion q1, VotedQuestion q2) {
            if (q1.voteCount != q2.voteCount) {
                return q1.voteCount > q2.voteCount ? 1 : -1;
            }
            if (q1.votedByMe != q2.votedByMe) {
                return q1.votedByMe ? 1 : -1;
            }
            if (q1.date != q2.date) {
                return q1.date > q2.date ? 1 : -1;
            }
            if (q1.key != q2.key) {
                return q1.key.compareTo(q2.key);
            }
            return 0;
        }
    }
}
