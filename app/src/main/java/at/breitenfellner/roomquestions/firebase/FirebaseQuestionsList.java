package at.breitenfellner.roomquestions.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import at.breitenfellner.roomquestions.model.Question;
import at.breitenfellner.roomquestions.model.Room;
import at.breitenfellner.roomquestions.state.QuestionsList;

/**
 * Firebase implementation of a questions list.
 */

public class FirebaseQuestionsList implements QuestionsList {
    private DatabaseReference dbReference;
    private HashMap<String, FirebaseRoomQuestionsList> roomLists;


    public FirebaseQuestionsList(DatabaseReference databaseReference, String root) {
        this.dbReference = databaseReference.child(root);
        roomLists = new HashMap<>();
    }

    private FirebaseRoomQuestionsList getRoomList(String roomKey) {
        FirebaseRoomQuestionsList roomList = null;

        if (roomLists.containsKey(roomKey)) {
            roomList = roomLists.get(roomKey);
        }
        else {
            roomList = new FirebaseRoomQuestionsList(roomKey);
            roomLists.put(roomKey, roomList);
        }
        return roomList;
    }

    @Override
    public List<Question> getQuestions(String roomKey) {
        return getRoomList(roomKey).questions;
    }

    @Override
    public void addQuestion(String roomKey, String text, String author) {
        getRoomList(roomKey).addQuestion(text, author);
    }


    @Override
    public void addChangeListener(String roomKey, ChangeListener changeListener) {
        getRoomList(roomKey).addChangeListener(changeListener);
    }

    @Override
    public void removeChangeListener(String roomKey, ChangeListener changeListener) {
        getRoomList(roomKey).removeChangeListener(changeListener);
    }

    class FirebaseRoomQuestionsList implements ValueEventListener {
        private String roomKey;
        private DatabaseReference dbQuestions;
        private List<Question> questions;
        private List<ChangeListener> listeners;


        FirebaseRoomQuestionsList(String roomKey) {
            this.dbQuestions = dbReference.child(roomKey);
            this.listeners = new ArrayList<>();
            this.roomKey = roomKey;
            dbQuestions.addValueEventListener(this);
        }

        private void notifyListeners() {
            for (ChangeListener listener : listeners) {
                listener.onChange(roomKey, FirebaseQuestionsList.this);
            }
        }

        void addQuestion(String text, String author) {
            Question question = new Question();
            question.text = text;
            question.author = author;
            question.owner = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference newNode = dbQuestions.push();
            question.key = newNode.getKey();
            question.date = System.currentTimeMillis() / 1000;
            newNode.setValue(question);
        }

        public void addChangeListener(ChangeListener changeListener) {
            listeners.add(changeListener);
        }

        public void removeChangeListener(ChangeListener changeListener) {
            listeners.remove(changeListener);
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
}
