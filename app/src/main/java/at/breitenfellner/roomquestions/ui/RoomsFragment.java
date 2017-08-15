package at.breitenfellner.roomquestions.ui;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import at.breitenfellner.roomquestions.R;
import at.breitenfellner.roomquestions.model.Room;
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
                    RoomsPagerAdapter adapter = new RoomsPagerAdapter(viewPager, getChildFragmentManager(), rooms);
                    // set adapter
                    viewPager.setAdapter(adapter);
                }
            }
        });
        return rootView;
    }
}
