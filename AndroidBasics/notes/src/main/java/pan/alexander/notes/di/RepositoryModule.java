package pan.alexander.notes.di;

import dagger.Module;
import dagger.Provides;
import pan.alexander.notes.data.MockNotesRepositoryImplementation;
import pan.alexander.notes.data.NotesRepositoryImplementation;
import pan.alexander.notes.domain.NotesRepository;

@Module
public class RepositoryModule {

    @Provides
    NotesRepository providesNotesRepository() {
        return new MockNotesRepositoryImplementation();
    }
}
