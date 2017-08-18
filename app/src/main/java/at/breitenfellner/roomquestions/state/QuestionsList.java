package at.breitenfellner.roomquestions.state;

import java.util.List;

import at.breitenfellner.roomquestions.model.Question;
import at.breitenfellner.roomquestions.model.Room;

/**
 * Interface to get the list of questions for a room.
 */

public interface QuestionsList {
    List<Question> getQuestions();

    void setRoom(Room room);

    void addQuestion(String text, String author);

    void addChangeListener(ChangeListener changeListener);

    void removeChangeListener(ChangeListener changeListener);

    interface ChangeListener {
        void onChange(QuestionsList questionsList);
    }
}
