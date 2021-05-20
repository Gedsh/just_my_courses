package pan.alexander.notes.di;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Lazy;
import pan.alexander.notes.data.database.NotesDao;
import pan.alexander.notes.domain.MainInteractor;

@Singleton
@Component(modules = {RepositoryModule.class, RoomModule.class})
public interface ApplicationComponent {
    Lazy<NotesDao> getNotesDao();
    Lazy<MainInteractor> getMainInteractor();
}