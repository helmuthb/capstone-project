package at.breitenfellner.roomquestions.di;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Named;
import javax.inject.Singleton;

import at.breitenfellner.roomquestions.firebase.FirebaseQuestionsList;
import at.breitenfellner.roomquestions.firebase.FirebaseRoomsList;
import at.breitenfellner.roomquestions.firebase.FirebaseUserAuthState;
import at.breitenfellner.roomquestions.firebase.FirebaseVotesMap;
import at.breitenfellner.roomquestions.state.QuestionsList;
import at.breitenfellner.roomquestions.state.RoomsList;
import at.breitenfellner.roomquestions.state.UserAuthState;
import at.breitenfellner.roomquestions.state.VotesMap;
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

    @Provides
    @Singleton
    static QuestionsList getQuestionsList(DatabaseReference dbReference, @Named("questions-root") String questionsRoot) {
        return new FirebaseQuestionsList(dbReference, questionsRoot);
    }

    @Provides
    @Named("questions-root")
    static String getQuestionsRoot() {
        return "questions";
    }

    @Provides
    @Singleton
    static VotesMap getVotesMap(DatabaseReference dbReference, @Named("votes-root") String votesRoot) {
        return new FirebaseVotesMap(dbReference, votesRoot);
    }

    @Provides
    @Named("votes-root")
    static String getVotesRoot() {
        return "votes";
    }
}
