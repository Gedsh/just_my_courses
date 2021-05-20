package pan.alexander.notes.data;

import androidx.lifecycle.LiveData;

import java.util.List;

import dagger.Lazy;
import pan.alexander.notes.App;
import pan.alexander.notes.data.database.NotesDao;
import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.domain.NotesRepository;

public class NotesRepositoryImplementation implements NotesRepository {
    private final Lazy<NotesDao> dao = App.getInstance().getDaggerComponent().getNotesDao();

    @Override
    public LiveData<List<Note>> getAllNotesFromDB() {
        return dao.get().getAllNotes();
    }

    @Override
    public void addNoteToDB(Note note) {
        dao.get().insertNote(note);
    }

    @Override
    public void updateNoteInDB(Note note) {
        dao.get().updateNote(note);
    }

    @Override
    public void removeNoteFromDB(Note note) {
        dao.get().deleteNote(note);
    }

    @Override
    public void removeAllNotesFromDB() {
        dao.get().deleteAllNotes();
    }
}
