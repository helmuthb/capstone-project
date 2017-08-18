package at.breitenfellner.roomquestions.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import at.breitenfellner.roomquestions.R;
import at.breitenfellner.roomquestions.model.Room;
import at.breitenfellner.roomquestions.util.KeyIdSource;

/**
 * Adapter to show different question lists
 */

class RoomsPagerAdapter extends FragmentPagerAdapter {
    private View parent;
    private List<Room> rooms;
    private List<QuestionsSubFragment> fragments;
    private KeyIdSource keyIdSource;

    RoomsPagerAdapter(View parent, FragmentManager fm, List<Room> rooms, KeyIdSource keyIdSource) {
        super(fm);
        this.parent = parent;
        this.rooms = rooms;
        this.fragments = new ArrayList<>();
        this.keyIdSource = keyIdSource;
    }

    @Override
    public long getItemId(int position) {
        return keyIdSource.getId(rooms.get(position).key);
    }

    @Override
    public Fragment getItem(int position) {
        if (position >= rooms.size()) {
            return null;
        }
        if (position >= fragments.size()) {
            fragments.add(position, null);
        }
        QuestionsSubFragment fragment = fragments.get(position);
        if (fragment == null) {
            fragment = QuestionsSubFragment.newInstance(rooms.get(position));
            fragments.add(position, fragment);
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Room room = rooms.get(position);
        if (room != null) {
            return parent.getResources().getString(R.string.room_name, room.name, room.description);
        }
        else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return rooms.size();
    }
}
