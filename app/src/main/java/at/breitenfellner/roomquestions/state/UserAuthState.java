package at.breitenfellner.roomquestions.state;

import at.breitenfellner.roomquestions.model.User;

/**
 * This interface describes the user authentication state.
 */

public interface UserAuthState {
    boolean isAuthenticated();

    User getUser();

    void addStateChangeListener(StateChangeListener listener);

    void removeStateChangeListener(StateChangeListener listener);

    interface StateChangeListener {
        void onStateChange(UserAuthState state);
    }
}
