package at.breitenfellner.roomquestions.di;

import javax.inject.Singleton;

import at.breitenfellner.roomquestions.util.KeyIdSource;
import at.breitenfellner.roomquestions.util.SimpleKeyIdSource;
import dagger.Module;
import dagger.Provides;

/**
 * This dagger module provides utility objects.
 */

@Module
public class UtilModule {
    @Provides
    @Singleton
    static KeyIdSource getIdSource() {
        return new SimpleKeyIdSource();
    }
}
