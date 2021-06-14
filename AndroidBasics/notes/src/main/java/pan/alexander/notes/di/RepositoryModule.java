package pan.alexander.notes.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pan.alexander.notes.data.NotesRepositoryImplementation;
import pan.alexander.notes.data.TrashRepositoryImplementation;
import pan.alexander.notes.domain.NotesRepository;
import pan.alexander.notes.domain.TrashRepository;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    NotesRepository providesNotesRepository() {
        return new NotesRepositoryImplementation();
    }

    @Provides
    @Singleton
    TrashRepository providesTrashRepository() {
        return new TrashRepositoryImplementation();
    }
}
