package at.breitenfellner.roomquestions.firebase;

import android.support.annotation.AnyThread;
import android.support.annotation.UiThread;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import at.breitenfellner.roomquestions.model.Question;
import at.breitenfellner.roomquestions.model.User;
import at.breitenfellner.roomquestions.state.VotesMap;

/**
 * Firebase implementation of votes map.
 */

public class FirebaseVotesMap implements VotesMap, ValueEventListener {
    private HashMap<String, List<String>> fullVotesMap;
    private List<ChangeListener> listeners;
    private DatabaseReference dbVotes;

    @UiThread
    public FirebaseVotesMap(DatabaseReference dbReference, String root) {
        listeners = new ArrayList<>();
        this.dbVotes = dbReference.child(root);
        // get notified on changes to the database
        dbVotes.addValueEventListener(this);
        fullVotesMap = new HashMap<>();
    }

    @AnyThread
    @Override
    public int getVoteCount(Question question) {
        if (question != null) {
            String questionKey = question.key;
            synchronized (fullVotesMap) {
                if (fullVotesMap.containsKey(questionKey)) {
                    return fullVotesMap.get(questionKey).size();
                }
            }
        }
        return 0;
    }

    @AnyThread
    @Override
    public boolean hasVoted(Question question, User user) {
        if (question != null && user != null) {
            String questionKey = question.key;
            synchronized (fullVotesMap) {
                if (fullVotesMap.containsKey(questionKey)) {
                    List<String> voters = fullVotesMap.get(questionKey);
                    return voters.contains(user.uid);
                }
            }
        }
        return false;
    }

    @UiThread
    @Override
    public void setVoted(Question question, User user, boolean hasVoted) {
        // use Firebase to add or remove a vote
        if (hasVoted) {
            dbVotes.child(question.key).child(user.uid).setValue(true);
        }
        else {
            dbVotes.child(question.key).child(user.uid).removeValue();
        }
    }

    @UiThread
    @Override
    public void addChangeListener(ChangeListener changeListener) {
        listeners.add(changeListener);
    }

    @UiThread
    @Override
    public void removeChangeListener(ChangeListener changeListener) {
        listeners.remove(changeListener);
    }

    @UiThread
    private void notifyListeners() {
        for (ChangeListener listener : listeners) {
            listener.onChange(this);
        }
    }

    @UiThread
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        synchronized (fullVotesMap) {
            fullVotesMap.clear();
            // children: id of the question
            for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                String questionKey = questionSnapshot.getKey();
                List<String> votes = new ArrayList<>();
                // children: id of the voters
                for (DataSnapshot voteSnapshot : questionSnapshot.getChildren()) {
                    String voterUid = voteSnapshot.getKey();
                    votes.add(voterUid);
                }
                fullVotesMap.put(questionKey, votes);
            }
        }
        notifyListeners();
    }

    @UiThread
    @Override
    public void onCancelled(DatabaseError databaseError) {
        synchronized (fullVotesMap) {
            fullVotesMap.clear();
        }
        notifyListeners();
    }
}
