package at.breitenfellner.roomquestions.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import at.breitenfellner.roomquestions.R;
import at.breitenfellner.roomquestions.model.Room;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter to show a room in a recycler view
 */

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {
    private List<Room> rooms;
    private RoomSelectionListener listener;

    RoomAdapter(List<Room> rooms, RoomSelectionListener listener) {
        this.rooms = rooms;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_home_room, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.bind(room);
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Room room;
        final RoomSelectionListener listener;
        @BindView(R.id.room_name)
        TextView roomName;
        @BindView(R.id.room_description)
                TextView roomDescription;
        @BindView(R.id.room_button)
        Button roomButton;

        ViewHolder(@NonNull View itemView, final RoomSelectionListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
            // call listener when button is clicked
            roomButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRoomSelected(room);
                }
            });
        }

        void bind(Room room) {
            this.room = room;
            if (room != null) {
                roomName.setText(room.name);
                roomDescription.setText(room.description);
                String buttonText = itemView.getResources().getString(R.string.goto_room, room.name);
                roomButton.setText(buttonText);
            }
        }
    }

    interface RoomSelectionListener {
        void onRoomSelected(Room room);
    }
}
