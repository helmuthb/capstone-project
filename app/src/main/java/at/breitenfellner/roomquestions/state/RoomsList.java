package at.breitenfellner.roomquestions.state;

import java.util.List;

import at.breitenfellner.roomquestions.model.Room;

/**
 * Interface to get the current set of rooms and information about them.
 */

public interface RoomsList {
    int getCount();
    List<Room> getRooms();
    void addChangeListener(ChangeListener changeListener);
    void removeChangeListener(ChangeListener changeListener);

    interface ChangeListener {
        void onChange(RoomsList roomsList);
    }
}
