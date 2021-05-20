package pan.alexander.notes.domain;

import androidx.lifecycle.LiveData;

import java.util.List;

import pan.alexander.notes.domain.entities.Note;

public interface NotesRepository {
    LiveData<List<Note>> getAllNotesFromDB();
    void addNoteToDB(Note note);
    void updateNoteInDB(Note note);
    void removeNoteFromDB(Note note);
    void removeAllNotesFromDB();
}
