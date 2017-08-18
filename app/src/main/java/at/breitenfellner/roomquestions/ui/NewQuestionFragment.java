package at.breitenfellner.roomquestions.ui;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import at.breitenfellner.roomquestions.R;
import at.breitenfellner.roomquestions.model.Room;
import at.breitenfellner.roomquestions.model.User;
import at.breitenfellner.roomquestions.state.QuestionsViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment for asking a new question.
 */

public class NewQuestionFragment extends LifecycleFragment {
    static final String ARG_ROOMKEY = "room_key";

    QuestionsViewModel viewModel;
    @BindView(R.id.room_name)
    TextView roomNameView;
    @BindView(R.id.field_question)
    EditText questionField;
    @BindView(R.id.field_name)
    EditText nameField;
    @BindView(R.id.switch_show_name)
    Switch showName;
    @BindView(R.id.button_submit)
    Button submit;
    String displayName;
    CloseQuestionOperation questionOperation;
    Room room;

    static NewQuestionFragment newInstance(Room room) {
        NewQuestionFragment fragment = new NewQuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ROOMKEY, room.key);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_question, container, false);
        ButterKnife.bind(this, rootView);
        // get view model
        viewModel = ViewModelProviders.of(getActivity()).get(QuestionsViewModel.class);
        // get argument
        final String roomKey = getArguments().getString(ARG_ROOMKEY);
        viewModel.getLiveRooms().observe(this, new Observer<List<Room>>() {
            @Override
            public void onChanged(@Nullable List<Room> rooms) {
                if (rooms != null && room == null) {
                    // now we know the rooms are known
                    room = viewModel.getRoom(roomKey);
                    // set text for room
                    String roomName = getResources().getString(R.string.room_name, room.name, room.description);
                    roomNameView.setText(roomName);
                    // set room into viewmodel
                    viewModel.setRoom(room);
                }
            }
        });
        viewModel.liveGetUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user != null && displayName == null) {
                    // get display name - first name if possible
                    displayName = user.name;
                    if (displayName.split("\\w+").length > 1) {
                        displayName = displayName.substring(0, displayName.lastIndexOf(' '));
                    }
                    showName.setChecked(true);
                }
            }
        });
        // listen on show-name button
        showName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    // show name edit field
                    nameField.setText(displayName);
                } else {
                    displayName = nameField.getText().toString();
                    nameField.setText(R.string.name_anonymous);
                }
                nameField.setEnabled(checked);
            }
        });
        // listen on submit
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionOperation != null) {
                    // save question
                    String text = questionField.getText().toString();
                    String author = nameField.getText().toString();
                    viewModel.addQuestion(text, author);
                    questionOperation.closeAskQuestion();
                }
            }
        });
        return rootView;
    }

    public void setQuestionOperation(CloseQuestionOperation questionOperation) {
        this.questionOperation = questionOperation;
    }

    interface CloseQuestionOperation {
        void closeAskQuestion();
    }
}
