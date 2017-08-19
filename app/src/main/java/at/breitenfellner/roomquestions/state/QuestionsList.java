package at.breitenfellner.roomquestions.state;

import java.util.List;

import at.breitenfellner.roomquestions.model.Question;

/**
 * Interface to get the list of questions for a room.
 */

public interface QuestionsList {
    List<Question> getQuestions(String roomKey);

    void addQuestion(String roomKey, String text, String author);

    void addChangeListener(String roomKey, ChangeListener changeListener);

    void removeChangeListener(String roomKey, ChangeListener changeListener);

    interface ChangeListener {
        void onChange(String roomKey, QuestionsList questionsList);
    }
}
