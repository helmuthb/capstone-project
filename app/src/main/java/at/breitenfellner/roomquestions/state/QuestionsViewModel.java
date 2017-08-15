package at.breitenfellner.roomquestions.state;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import at.breitenfellner.roomquestions.di.DaggerRoomQuestionsComponent;
import at.breitenfellner.roomquestions.di.RoomQuestionsComponent;
import at.breitenfellner.roomquestions.model.Question;
import at.breitenfellner.roomquestions.model.Room;
import at.breitenfellner.roomquestions.util.KeyIdSource;

/**
 * ViewModel class for the questions of a specific room.
 */

public class QuestionsViewModel extends MainViewModel
        implements QuestionsList.ChangeListener {
    private Room room;
    @Inject
    QuestionsList questionsList;
    private MutableLiveData<List<Question>> liveQuestions;

    public QuestionsViewModel() {
        super();
        RoomQuestionsComponent component = DaggerRoomQuestionsComponent.builder().build();
        component.injectQuestions(this);
        liveQuestions = new MutableLiveData<>();
        questionsList.addChangeListener(this);
    }

    public void setRoom(Room room) {
        this.room = room;
        questionsList.setRoom(room);
    }

    public LiveData<List<Question>> getLiveQuestions() {
        return liveQuestions;
    }

    @Override
    public void onChange(QuestionsList questionsList) {
        List<Question> questions = questionsList.getQuestions();
        liveQuestions.setValue(questions);
    }

}
