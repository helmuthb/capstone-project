package at.breitenfellner.roomquestions.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.HashMap;
import java.util.List;

import at.breitenfellner.roomquestions.R;
import at.breitenfellner.roomquestions.model.Room;
import at.breitenfellner.roomquestions.model.VotedQuestion;
import at.breitenfellner.roomquestions.state.QuestionsViewModel;
import timber.log.Timber;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link RoomWidgetConfigureActivity RoomWidgetConfigureActivity}
 */
public class RoomWidget extends AppWidgetProvider {
    private final static String WIDGET_ROOM_KEY = "room_key";
    static HashMap<String, QuestionsViewModel> viewModelHashMap;

    static QuestionsViewModel getViewModel(String roomKey) {
        QuestionsViewModel viewModel;

        if (roomKey != null) {
            if (viewModelHashMap == null) {
                viewModelHashMap = new HashMap<>();
            }
            if (viewModelHashMap.containsKey(roomKey)) {
                viewModel = viewModelHashMap.get(roomKey);
            } else {
                viewModel = new QuestionsViewModel();
                viewModel.setRoom(roomKey);
                viewModelHashMap.put(roomKey, viewModel);
            }
            return viewModel;
        }
        return null;
    }

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        final String roomKey = RoomWidgetConfigureActivity.loadRoomkeyPref(context, appWidgetId);
        Intent appIntent = new Intent(context, MainActivity.class);
        appIntent.putExtra(MainActivity.ARG_ROOMKEY, roomKey);
        appIntent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        Timber.d("Room key: " + roomKey);
        final PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                appWidgetId,
                appIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        if (roomKey != null) {
            getViewModel(roomKey).getLiveRooms().observeForever(new Observer<List<Room>>() {
                @Override
                public void onChanged(@Nullable List<Room> rooms) {
                    if (rooms != null) {
                        getViewModel(roomKey).getLiveQuestions().observeForever(new Observer<List<VotedQuestion>>() {
                            @Override
                            public void onChanged(@Nullable List<VotedQuestion> votedQuestions) {
                                if (votedQuestions != null) {
                                    // rooms & questions are here!
                                    Room room = getViewModel(roomKey).getRoom(roomKey);
                                    // Construct the RemoteViews object
                                    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
                                    if (room != null) {
                                        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
                                        views.setPendingIntentTemplate(R.id.question_list, pendingIntent);
                                        views.setTextViewText(R.id.room_name, room.name);
                                        views.setTextViewText(R.id.room_description, room.description);

                                        // set adapter for list view
                                        Intent listIntent = new Intent(context, WidgetService.class);
                                        listIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                                        listIntent.putExtra(WIDGET_ROOM_KEY, roomKey);
                                        listIntent.setData(Uri.parse(listIntent.toUri(Intent.URI_INTENT_SCHEME)));
                                        views.setRemoteAdapter(R.id.question_list, listIntent);

                                        // Instruct the widget manager to update the widget
                                        appWidgetManager.updateAppWidget(appWidgetId, views);
                                        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.question_list);
                                    }
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            RoomWidgetConfigureActivity.deleteRoomkeyPref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static class WidgetService extends RemoteViewsService {
        @Override
        public RemoteViewsFactory onGetViewFactory(Intent intent) {
            // get widget id from intent
            int appWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            // get room key from intent
            String roomKey = intent.getStringExtra(WIDGET_ROOM_KEY);
            return (new QuestionsRemoteViewsFactory(this.getApplicationContext(), roomKey));
        }
    }
}

