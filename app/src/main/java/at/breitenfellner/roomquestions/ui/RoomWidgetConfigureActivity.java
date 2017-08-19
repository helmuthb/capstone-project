package at.breitenfellner.roomquestions.ui;

import android.arch.lifecycle.LifecycleActivity;

import at.breitenfellner.roomquestions.state.MainViewModel;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import at.breitenfellner.roomquestions.R;
import at.breitenfellner.roomquestions.model.Room;

/**
 * The configuration screen for the {@link RoomWidget RoomWidget} AppWidget.
 */
public class RoomWidgetConfigureActivity extends LifecycleActivity {

    private MainViewModel viewModel;
    private static final String PREFS_NAME = "at.breitenfellner.roomquestions.ui.RoomWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    RoomWidgetConfigureRoomsAdapter.RoomSelectionCallback addWidgetListener = new RoomWidgetConfigureRoomsAdapter.RoomSelectionCallback() {
        @Override
        public void onRoomSelected(Room room) {
            final Context context = RoomWidgetConfigureActivity.this;

            // save room key
            saveRoomkeyPref(context, widgetId, room.key);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RoomWidget.updateAppWidget(context, appWidgetManager, widgetId);

            // Make sure we pass back the original widgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public RoomWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveRoomkeyPref(Context context, int appWidgetId, String key) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, key);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadRoomkeyPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return null;
        }
    }

    static void deleteRoomkeyPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        // get the view model
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        setContentView(R.layout.activity_widget_config);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        // add listener on cancel button
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // show the rooms
        viewModel.getLiveRooms().observe(this, new Observer<List<Room>>() {
            @Override
            public void onChanged(@Nullable List<Room> rooms) {
                if (rooms != null) {
                    RoomWidgetConfigureRoomsAdapter adapter = new RoomWidgetConfigureRoomsAdapter(
                            rooms,
                            viewModel.getKeyIdSource(),
                            addWidgetListener);
                    RecyclerView recyclerView = findViewById(R.id.rooms_recyclerview);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }
}

