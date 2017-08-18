package at.breitenfellner.roomquestions.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import at.breitenfellner.roomquestions.model.Room;
import at.breitenfellner.roomquestions.state.RoomsList;

/**
 * Firebase implementation of the RoomsList interface.
 */

public class FirebaseRoomsList implements RoomsList {
    private List<Room> rooms;
    private List<ChangeListener> listeners;

    public FirebaseRoomsList(DatabaseReference dbReference, String root) {
        listeners = new ArrayList<>();
        // get notified on changes to the database
        dbReference.child(root).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // load the data
                rooms = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    rooms.add(child.getValue(Room.class));
                }
                // notify the listeners
                for (ChangeListener listener : listeners) {
                    listener.onChange(FirebaseRoomsList.this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // create empty room list
                rooms = new ArrayList<>();
                // notify the listeners
                for (ChangeListener listener : listeners) {
                    listener.onChange(FirebaseRoomsList.this);
                }
            }
        });
    }

    @Override
    public List<Room> getRooms() {
        return rooms;
    }

    @Override
    public void addChangeListener(ChangeListener changeListener) {
        listeners.add(changeListener);
    }

    @Override
    public void removeChangeListener(ChangeListener changeListener) {
        listeners.remove(changeListener);
    }
}
