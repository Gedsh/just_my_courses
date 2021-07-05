package pan.alexander.notes.domain;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import pan.alexander.notes.domain.account.User;
import pan.alexander.notes.domain.entities.Note;

public interface NotesRepository {
    LiveData<List<Note>> getAllNotesFromDB();

    Completable addNoteToDB(Note note);

    Completable addNotesToDB(List<Note> notes);

    Completable updateNoteInDB(Note note);

    Completable removeNoteFromDB(Note note);

    Completable removeEmptyNotesFromDBExceptLast();

    Completable removeNotesFromDB(List<Note> notes);

    Completable removeAllNotesFromDB();
}
