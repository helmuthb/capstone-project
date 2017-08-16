package at.breitenfellner.roomquestions.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import at.breitenfellner.roomquestions.model.User;
import at.breitenfellner.roomquestions.state.UserAuthState;

/**
 * This is the implementation of a UserAuthState based on Firebase Authentication
 */

public class FirebaseUserAuthState implements UserAuthState, FirebaseAuth.AuthStateListener {
    private User user;
    private List<StateChangeListener> listeners;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    public FirebaseUserAuthState(FirebaseAuth auth) {
        // add a listener: note when authentication state changes
        user = null;
        listeners = new ArrayList<>();
        auth.addAuthStateListener(this);
    }

    @Override
    public boolean isAuthenticated() {
        return user != null;
    }

    @Override
    @Nullable
    public User getUser() {
        return user;
    }

    @Override
    public void addStateChangeListener(StateChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeStateChangeListener(StateChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            user = null;
        }
        else {
            String name = firebaseUser.getDisplayName();
            android.net.Uri photoUrl = firebaseUser.getPhotoUrl();
            String uid = firebaseUser.getUid();
            user = new User(name, photoUrl, uid);
        }
        // notify all listeners
        for (StateChangeListener listener : listeners) {
            if (listener != null) {
                listener.onStateChange(FirebaseUserAuthState.this);
            }
        }
    }
}
