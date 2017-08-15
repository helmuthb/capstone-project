package at.breitenfellner.roomquestions.di;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Named;
import javax.inject.Singleton;

import at.breitenfellner.roomquestions.firebase.FirebaseRoomsList;
import at.breitenfellner.roomquestions.firebase.FirebaseUserAuthState;
import at.breitenfellner.roomquestions.state.RoomsList;
import at.breitenfellner.roomquestions.state.UserAuthState;
import dagger.Module;
import dagger.Provides;

/**
 * This dagger module provides firebase related classes
 */

@Module
public class FirebaseModule {
    @Provides
    @Singleton
    static FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    static UserAuthState getUserAuthState(FirebaseAuth auth) {
        return new FirebaseUserAuthState(auth);
    }

    @Provides
    @Singleton
    static DatabaseReference getDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference();
    }

    @Provides
    @Singleton
    static RoomsList getRoomsList(DatabaseReference dbReference, @Named("rooms-root") String roomsRoot) {
        return new FirebaseRoomsList(dbReference, roomsRoot);
    }

    @Provides
    @Named("rooms-root")
    static String getRoomsRoot() {
        return "rooms";
    }
}
