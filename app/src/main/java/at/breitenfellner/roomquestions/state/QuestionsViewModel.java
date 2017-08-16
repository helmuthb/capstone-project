package at.breitenfellner.roomquestions.state;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.support.annotation.AnyThread;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import at.breitenfellner.roomquestions.di.DaggerRoomQuestionsComponent;
import at.breitenfellner.roomquestions.di.RoomQuestionsComponent;
import at.breitenfellner.roomquestions.model.Question;
import at.breitenfellner.roomquestions.model.Room;
import at.breitenfellner.roomquestions.model.User;
import at.breitenfellner.roomquestions.model.VotedQuestion;

/**
 * ViewModel class for the questions of a specific room.
 */

public class QuestionsViewModel extends MainViewModel
        implements QuestionsList.ChangeListener, VotesMap.ChangeListener {
    private Room room;
    @Inject
    QuestionsList questionsList;
    @Inject
    VotesMap votesMap;
    private MutableLiveData<List<VotedQuestion>> liveQuestions;

    @UiThread
    public QuestionsViewModel() {
        super();
        RoomQuestionsComponent component = DaggerRoomQuestionsComponent.builder().build();
        component.injectQuestions(this);
        liveQuestions = new MutableLiveData<>();
        questionsList.addChangeListener(this);
        votesMap.addChangeListener(this);
    }

    @UiThread
    public void setRoom(Room room) {
        this.room = room;
        questionsList.setRoom(room);
    }

    @UiThread
    public LiveData<List<VotedQuestion>> getLiveQuestions() {
        return liveQuestions;
    }

    @UiThread
    private void sortQuestions(List<Question> newQuestions) {
        // merge questions with votes, sort them
        // all in an AsyncTask
        // update the liveQuestions LiveData afterwards in the UI thread
        new AsyncTask<List<Question>, Void, List<VotedQuestion>>() {

            @UiThread
            @Override
            protected void onPostExecute(List<VotedQuestion> votedQuestions) {
                liveQuestions.setValue(votedQuestions);
            }

            @WorkerThread
            @Override
            protected List<VotedQuestion> doInBackground(List<Question>... lists) {
                List<Question> currentQuestions = lists[0];
                List<VotedQuestion> votedQuestions = new ArrayList<>();
                if (currentQuestions != null) {
                    for (Question q : currentQuestions) {
                        VotedQuestion votedQuestion = new VotedQuestion(q);
                        votedQuestion.voteCount = votesMap.getVoteCount(q);
                        votedQuestion.votedByMe = votesMap.hasVoted(q, getUser());
                        votedQuestions.add(votedQuestion);
                    }
                    Collections.sort(votedQuestions, new VotedQuestion.Comparator());
                }
                return votedQuestions;
            }
        }.execute(newQuestions);
    }

    @UiThread
    public void setVote(Question question, boolean voted) {
        User user = getUser();
        if (user != null) {
            votesMap.setVoted(question, user, voted);
        }
    }

    @UiThread
    @Override
    public void onChange(QuestionsList questionsList) {
        sortQuestions(questionsList.getQuestions());
    }

    @UiThread
    @Override
    public void onChange(VotesMap votesMap) {
        sortQuestions(questionsList.getQuestions());
    }
}
