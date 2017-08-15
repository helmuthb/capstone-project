package at.breitenfellner.roomquestions.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import at.breitenfellner.roomquestions.model.Question;
import at.breitenfellner.roomquestions.model.Room;
import at.breitenfellner.roomquestions.state.QuestionsList;

/**
 * Firebase implementation of a questions list.
 */

public class FirebaseQuestionsList implements QuestionsList, ValueEventListener {
    private List<Question> questions;
    private List<ChangeListener> listeners;
    private DatabaseReference dbReference;
    private String roomKey;

    public FirebaseQuestionsList(DatabaseReference databaseReference, String root) {
        dbReference = databaseReference.child(root);
        listeners = new ArrayList<>();
        roomKey = null;
    }
    @Override
    public List<Question> getQuestions() {
        return questions;
    }

    @Override
    public void setRoom(Room room) {
        if (room != null && !room.key.equals(roomKey)) {
            if (roomKey != null) {
                // remove me as a listener from old position
                dbReference.child(roomKey).removeEventListener(this);
            }
            roomKey = room.key;
            dbReference.child(roomKey).addValueEventListener(this);
        }
        else if (room == null && roomKey != null) {
            dbReference.child(roomKey).removeEventListener(this);
            roomKey = null;
        }
    }

    @Override
    public void addChangeListener(ChangeListener changeListener) {
        listeners.add(changeListener);
    }

    @Override
    public void removeChangeListener(ChangeListener changeListener) {
        listeners.remove(changeListener);
    }

    private void notifyListeners() {
        for (ChangeListener listener : listeners) {
            listener.onChange(this);
        }
    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        questions = new ArrayList<>();
        // load data
        for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
            Question question = questionSnapshot.getValue(Question.class);
            questions.add(question);
        }
        // notify child listeners
        notifyListeners();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        questions = new ArrayList<>();
        notifyListeners();
    }
}
