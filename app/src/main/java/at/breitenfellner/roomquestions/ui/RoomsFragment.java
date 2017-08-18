package at.breitenfellner.roomquestions.ui;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import at.breitenfellner.roomquestions.R;
import at.breitenfellner.roomquestions.model.Room;
import at.breitenfellner.roomquestions.model.User;
import at.breitenfellner.roomquestions.state.MainViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment showing tabs for the rooms and in them the currently asked questions.
 */

public class RoomsFragment extends LifecycleFragment {
    MainViewModel viewModel;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.textview_why_login)
    TextView whyLoginText;
    @BindView(R.id.button_login)
    Button buttonLogin;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    OpenQuestionOperation questionOperation;
    List<Room> rooms;

    public void setQuestionOperations(OpenQuestionOperation questionOperation) {
        this.questionOperation = questionOperation;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rooms, container, false);
        ButterKnife.bind(this, rootView);
        // get view model
        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        // subscribe on room changes
        viewModel.getLiveRooms().observe(this, new Observer<List<Room>>() {
            @Override
            public void onChanged(@Nullable List<Room> rooms) {
                // create pager adapter
                if (rooms != null) {
                    RoomsFragment.this.rooms = rooms;
                    RoomsPagerAdapter adapter = new RoomsPagerAdapter(viewPager, getChildFragmentManager(), rooms, viewModel.getKeyIdSource());
                    // set adapter
                    viewPager.setAdapter(adapter);
                    // set first page - delayed after a layout phase
                    final int currentRoom = viewModel.getCurrentRoomPosition();
                    if (currentRoom < rooms.size()) {
                        viewPager.post(new Runnable() {
                            @Override
                            public void run() {
                                viewPager.setCurrentItem(currentRoom, false);
                            }
                        });
                    }
                }
            }
        });
        // subscribe to changes in the ViewPager
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // don't care
            }

            @Override
            public void onPageSelected(int position) {
                viewModel.setCurrentRoomPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // don't care
            }
        });
        // subscribe to changes in the login state
        viewModel.liveGetUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user == null) {
                    // show login button
                    buttonLogin.setVisibility(View.VISIBLE);
                    whyLoginText.setVisibility(View.VISIBLE);
                } else {
                    // hide login button
                    buttonLogin.setVisibility(View.GONE);
                    whyLoginText.setVisibility(View.GONE);
                }
            }
        });
        // add login button functionality
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.startLogin(getActivity());
            }
        });
        // add FAB functionality
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open question if possible
                if (questionOperation != null) {
                    int roomPosition = viewModel.getCurrentRoomPosition();
                    if (rooms != null && roomPosition < rooms.size() && roomPosition >= 0) {
                        Room room = rooms.get(roomPosition);
                        questionOperation.openAskQuestion(room);
                    }
                }
            }
        });
        return rootView;
    }

    interface OpenQuestionOperation {
        void openAskQuestion(Room room);
    }
}
