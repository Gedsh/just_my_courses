package pan.alexander.notes.domain.entities;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface NotesRepository {
    LiveData<List<Note>> getAllNotesFromDB();
    void addNoteToDB(Note note);
    void updateNoteInDB(Note note);
    void removeNoteFromDB(Note note);
    void removeAllNotesFromDB();
}
