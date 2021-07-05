package pan.alexander.notes.di;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pan.alexander.notes.data.database.FirestoreDatabase;
import pan.alexander.notes.data.database.NotesDao;

@Module
public class FirestoreModule {
    FirestoreDatabase db;

    @Inject
    public FirestoreModule(FirestoreDatabase db) {
        this.db = db;
    }
}
