package at.breitenfellner.roomquestions.di;

import javax.inject.Singleton;

import at.breitenfellner.roomquestions.state.MainViewModel;
import dagger.Component;

/**
 * Created by helmuth on 14/08/2017.
 */

@Singleton
@Component(modules = { FirebaseModule.class, UtilModule.class })
public interface RoomQuestionsComponent {
    // MainViewModel needs user authentication state
    void inject(MainViewModel mainViewModel);
}
