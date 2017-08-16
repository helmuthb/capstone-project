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
import at.breitenfellner.roomquestions.util.KeyIdSource;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter to show a room in a recycler view
 */

class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder> {
    private List<Room> rooms;
    private RoomSelectionListener listener;
    private KeyIdSource keyIdSource;

    RoomsAdapter(List<Room> rooms, RoomSelectionListener listener, KeyIdSource keyIdSource) {
        super();
        this.rooms = rooms;
        this.listener = listener;
        this.keyIdSource = keyIdSource;
        setHasStableIds(true);
    }

    void setRoomSelectionListener(RoomSelectionListener listener) {
        this.listener = listener;
    }

    @Override
    public long getItemId(int position) {
        return keyIdSource.getId(rooms.get(position).key);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_home_room, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.bind(room, position);
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Room room;
        @BindView(R.id.room_name)
        TextView roomName;
        @BindView(R.id.room_description)
        TextView roomDescription;
        @BindView(R.id.room_button)
        Button roomButton;
        int position;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            // call listener when button is clicked
            roomButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onRoomSelected(room, position);
                    }
                }
            });
        }

        void bind(Room room, int position) {
            this.room = room;
            this.position = position;
            if (room != null) {
                roomName.setText(room.name);
                roomDescription.setText(room.description);
                String buttonText = itemView.getResources().getString(R.string.goto_room, room.name);
                roomButton.setText(buttonText);
            }
        }
    }

    interface RoomSelectionListener {
        void onRoomSelected(Room room, int position);
    }
}
