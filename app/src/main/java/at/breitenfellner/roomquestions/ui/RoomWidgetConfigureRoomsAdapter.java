package at.breitenfellner.roomquestions.ui;

import android.content.Context;
import android.content.res.Resources;
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
 * Created by helmuth on 19/08/2017.
 */

public class RoomWidgetConfigureRoomsAdapter extends RecyclerView.Adapter<RoomWidgetConfigureRoomsAdapter.ViewHolder> {

    private List<Room> rooms;
    private KeyIdSource keyIdSource;
    private RoomSelectionCallback callback;

    public RoomWidgetConfigureRoomsAdapter(List<Room> rooms, KeyIdSource keyIdSource, RoomSelectionCallback callback) {
        super();
        this.rooms = rooms;
        this.keyIdSource = keyIdSource;
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_widget_config_room, parent, false);
        return new ViewHolder(view);
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

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.room_name)
        TextView roomName;
        @BindView(R.id.room_description)
        TextView roomDescription;
        @BindView(R.id.room_button)
        Button roomButton;
        Resources resources;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            resources = itemView.getResources();
        }

        void bind(final Room room) {
            roomName.setText(room.name);
            roomDescription.setText(room.description);
            String buttonText = resources.getString(R.string.use_room, room.name);
            roomButton.setText(buttonText);
            roomButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onRoomSelected(room);
                }
            });
        }
    }

    public interface RoomSelectionCallback {
        void onRoomSelected(Room room);
    }
}
