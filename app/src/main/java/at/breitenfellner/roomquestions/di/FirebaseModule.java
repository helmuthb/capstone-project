package at.breitenfellner.roomquestions.di;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Singleton;

import at.breitenfellner.roomquestions.firebase.FirebaseUserAuthState;
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
}
