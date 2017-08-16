package at.breitenfellner.roomquestions.state;

import android.support.annotation.AnyThread;
import android.support.annotation.UiThread;

import at.breitenfellner.roomquestions.model.Question;
import at.breitenfellner.roomquestions.model.User;

/**
 * Mapping from question to voters information
 */

public interface VotesMap {
    @AnyThread
    int getVoteCount(Question question);

    @AnyThread
    boolean hasVoted(Question question, User user);

    @UiThread
    void setVoted(Question question, User user, boolean hasVoted);

    @UiThread
    void addChangeListener(ChangeListener changeListener);

    @UiThread
    void removeChangeListener(ChangeListener changeListener);

    interface ChangeListener {
        @UiThread
        void onChange(VotesMap votesMap);
    }
}
