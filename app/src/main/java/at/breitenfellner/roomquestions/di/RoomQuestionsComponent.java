package at.breitenfellner.roomquestions.di;

import javax.inject.Singleton;

import at.breitenfellner.roomquestions.state.MainViewModel;
import at.breitenfellner.roomquestions.state.QuestionsViewModel;
import dagger.Component;

/**
 * Dagger component which will inject all needed dependencies.
 */

@Singleton
@Component(modules = {FirebaseModule.class, UtilModule.class})
public interface RoomQuestionsComponent {
    // MainViewModel needs user authentication state and rooms list
    void inject(MainViewModel mainViewModel);

    // QuestionsViewModel needs questions list
    void injectQuestions(QuestionsViewModel questionsViewModel);
}
