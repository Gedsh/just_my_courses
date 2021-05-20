package pan.alexander.notes.domain;

import androidx.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import pan.alexander.notes.domain.entities.Note;

@Singleton
public class MainInteractor {
    private final NotesRepository notesRepository;

    @Inject
    public MainInteractor(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    public LiveData<List<Note>> getAllNotes() {
        return notesRepository.getAllNotesFromDB();
    }

    public void updateNote(Note note) {
        notesRepository.updateNoteInDB(note);
    }

    public void saveNote(Note note) {
        notesRepository.addNoteToDB(note);
    }

    public void removeNote(Note note) {
        notesRepository.removeNoteFromDB(note);
    }

    public void removeAllNotes() {
        notesRepository.removeAllNotesFromDB();
    }
}
