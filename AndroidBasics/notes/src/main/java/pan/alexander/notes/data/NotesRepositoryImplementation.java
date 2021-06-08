package pan.alexander.notes.data;

import androidx.lifecycle.LiveData;

import java.util.List;

import dagger.Lazy;
import io.reactivex.Completable;
import io.reactivex.Single;
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
    public Single<List<Note>> getNoteByTime(long time) {
        return dao.get().getNoteByTime(time);
    }

    @Override
    public Completable addNoteToDB(Note note) {
        return dao.get().insertNote(note);
    }

    @Override
    public Completable addNotesToDB(List<Note> notes) {
        return dao.get().insertNotes(notes);
    }

    @Override
    public Completable updateNoteInDB(Note note) {
        return dao.get().updateNote(note);
    }

    @Override
    public Completable removeNoteFromDB(Note note) {
        return dao.get().deleteNote(note);
    }

    @Override
    public Completable removeEmptyNotesFromDBExceptLast() {
        return dao.get().deleteEmptyNotesExceptLast();
    }

    @Override
    public Completable removeNotesFromDB(List<Note> notes) {
        return dao.get().deleteNotes(notes);
    }

    @Override
    public Completable removeAllNotesFromDB() {
        return dao.get().deleteAllNotes();
    }
}
