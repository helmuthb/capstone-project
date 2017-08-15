package at.breitenfellner.roomquestions.state;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.v4.app.FragmentActivity;

import com.firebase.ui.auth.AuthUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import at.breitenfellner.roomquestions.di.DaggerRoomQuestionsComponent;
import at.breitenfellner.roomquestions.di.RoomQuestionsComponent;
import at.breitenfellner.roomquestions.model.Room;
import at.breitenfellner.roomquestions.model.User;
import at.breitenfellner.roomquestions.util.KeyIdSource;
import timber.log.Timber;

/**
 * The view model used for the main activity.
 */

public class MainViewModel extends AndroidViewModel
        implements UserAuthState.StateChangeListener, RoomsList.ChangeListener {
    public static final int REQUESTCODE_AUTH = 42;
    @Inject
    UserAuthState userAuthState;
    @Inject
    KeyIdSource keyIdSource;
    @Inject
    RoomsList roomsList;
    private boolean loggedIn;
    private MutableLiveData<Boolean> liveLoggedIn;
    private User user;
    private MutableLiveData<User> liveUser;
    private List<Room> rooms;
    private MutableLiveData<List<Room>> liveRooms;

    public MainViewModel(Application application) {
        super(application);
        RoomQuestionsComponent component = DaggerRoomQuestionsComponent.builder().build();
        component.inject(this);
        liveLoggedIn = new MutableLiveData<>();
        loggedIn = false;
        liveLoggedIn.setValue(loggedIn);
        liveUser = new MutableLiveData<>();
        user = null;
        liveUser.setValue(user);
        rooms = new ArrayList<>();
        liveRooms = new MutableLiveData<>();
        liveRooms.setValue(rooms);
        // add listener on authentication status
        userAuthState.addStateChangeListener(this);
        // add listener on room changes
        roomsList.addChangeListener(this);
    }

    private void setIsLoggedIn(boolean value) {
        if (value != loggedIn) {
            loggedIn = value;
            liveLoggedIn.setValue(loggedIn);
        }
    }

    private void setUser(User user) {
        if (user != this.user) {
            this.user = user;
            liveUser.setValue(user);
        }
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public LiveData<List<Room>> getLiveRooms() {
        return liveRooms;
    }

    public long getRoomId(Room room) {
        if (room != null && room.key != null) {
            return keyIdSource.getId(room.key);
        }
        return -1L;
    }

    public LiveData<Boolean> liveIsLoggedIn() {
        return liveLoggedIn;
    }


    public User getUser() {
        return user;
    }

    public LiveData<User> liveGetUser() {
        return liveUser;
    }

    public void startLogin(Activity activity) {
        if (isLoggedIn()) {
            return;
        }
        activity.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build()))
                        .build(),
                REQUESTCODE_AUTH);
    }

    public void performLogout(FragmentActivity activity) {
        // perform signout
        AuthUI.getInstance().signOut(activity);
    }

    @Override
    public void onStateChange(UserAuthState state) {
        setIsLoggedIn(state.isAuthenticated());
        setUser(state.getUser());
        if (state.getUser() == null) {
            Timber.d("Received new user state: null");
        }
        else {
            Timber.d("Received new user state: %s", state.getUser().name);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        userAuthState.removeStateChangeListener(this);
    }

    @Override
    public void onChange(RoomsList roomsList) {
        rooms = roomsList.getRooms();
        liveRooms.setValue(rooms);
    }
}
