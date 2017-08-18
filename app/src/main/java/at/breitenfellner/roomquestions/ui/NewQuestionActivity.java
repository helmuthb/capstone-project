package at.breitenfellner.roomquestions.ui;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.List;

import at.breitenfellner.roomquestions.R;
import at.breitenfellner.roomquestions.model.Room;
import at.breitenfellner.roomquestions.state.QuestionsViewModel;

public class NewQuestionActivity extends AppCompatActivity implements NewQuestionFragment.CloseQuestionOperation, LifecycleRegistryOwner {
    QuestionsViewModel viewModel;
    LifecycleRegistry registry;
    Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);
        // get view model
        viewModel = ViewModelProviders.of(this).get(QuestionsViewModel.class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            Bundle args = getIntent().getExtras();
            if (args != null && args.containsKey(NewQuestionFragment.ARG_ROOMKEY)) {
                final String roomKey = args.getString(NewQuestionFragment.ARG_ROOMKEY);
                viewModel.getLiveRooms().observe(this, new Observer<List<Room>>() {
                    @Override
                    public void onChanged(@Nullable List<Room> rooms) {
                        if (rooms != null && room == null) {
                            room = viewModel.getRoom(roomKey);
                            NewQuestionFragment fragment = NewQuestionFragment.newInstance(room);
                            fragment.setQuestionOperation(NewQuestionActivity.this);
                            FragmentManager fm = getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.add(R.id.fragment_container, fragment);
                            ft.commit();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void closeAskQuestion() {
        finish();
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        if (registry == null) {
            registry = new LifecycleRegistry(this);
        }
        return registry;
    }
}
