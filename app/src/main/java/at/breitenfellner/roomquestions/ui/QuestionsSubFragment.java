package at.breitenfellner.roomquestions.ui;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import at.breitenfellner.roomquestions.R;
import at.breitenfellner.roomquestions.model.Room;
import at.breitenfellner.roomquestions.model.User;
import at.breitenfellner.roomquestions.model.VotedQuestion;
import at.breitenfellner.roomquestions.state.QuestionsViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Subfragment used to show questions.
 */

public class QuestionsSubFragment extends LifecycleFragment implements QuestionsAdapter.QuestionClickListener {
    static final String ARG_ROOMKEY = "room_key";

    QuestionsViewModel viewModel;
    String roomKey;
    @BindView(R.id.questions_recyclerview)
    RecyclerView questionsView;
    QuestionsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get view model - it is specific per fragment
        viewModel = ViewModelProviders.of(this).get(QuestionsViewModel.class);
        // get the room key
        roomKey = getArguments().getString(ARG_ROOMKEY);
        adapter = new QuestionsAdapter(this, viewModel.getKeyIdSource());
        viewModel.setRoom(roomKey);
    }

    static QuestionsSubFragment newInstance(Room room) {
        QuestionsSubFragment fragment = new QuestionsSubFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ROOMKEY, room.key);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.subfragment_questions, container, false);
        ButterKnife.bind(this, rootView);
        // set recyclerview adapter
        Timber.d("Adding the adapter for questions!");
        questionsView.setAdapter(adapter);
        // listen for changes
        Timber.d("Waiting for changes of questions...");
        viewModel.getLiveQuestions().observe(this, new Observer<List<VotedQuestion>>() {
            @Override
            public void onChanged(@Nullable List<VotedQuestion> questions) {
                if (questions != null) {
                    // question list has changed
                    Timber.d("New questions list arrived");
                    adapter.updateQuestionsList(questions);
                }
            }
        });
        // watch for changes in login status
        viewModel.liveGetUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                adapter.setLoginStatus(user != null);
            }
        });
        return rootView;

    }

    @Override
    public void onQuestionClicked(VotedQuestion question) {
        viewModel.setVote(question, !question.votedByMe);
    }
}
