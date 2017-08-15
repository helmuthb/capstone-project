package at.breitenfellner.roomquestions;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

import timber.log.Timber;

/**
 * Android app - creating Timber logging instance
 */

public class RoomQuestionsApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
