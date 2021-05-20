package pan.alexander.notes.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import pan.alexander.notes.domain.NotesRepository;
import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.domain.entities.NoteType;

public class MockNotesRepositoryImplementation implements NotesRepository {
    private MutableLiveData<List<Note>> notesLiveData;
    private final List<Note> notes = new ArrayList<Note>(){{
        add(new Note("Monday", "Wake up", NoteType.TEXT_NOTE, System.currentTimeMillis(), "#FFFFFF"));
        add(new Note("Tuesday", "Ready", NoteType.TEXT_NOTE, System.currentTimeMillis(), "#FFFFFF"));
        add(new Note("Wednesday", "Work", NoteType.TEXT_NOTE, System.currentTimeMillis(), "#FFFFFF"));
        add(new Note("Thursday", "Tired", NoteType.TEXT_NOTE, System.currentTimeMillis(), "#FFFFFF"));
        add(new Note("Friday", "Wait", NoteType.TEXT_NOTE, System.currentTimeMillis(), "#FFFFFF"));
        add(new Note("Saturday", "Play", NoteType.TEXT_NOTE, System.currentTimeMillis(), "#FFFFFF"));
        add(new Note("Sunday", "Sleep", NoteType.TEXT_NOTE, System.currentTimeMillis(), "#FFFFFF"));
    }};

    @Override
    public LiveData<List<Note>> getAllNotesFromDB() {

        if (notesLiveData == null) {
            notesLiveData = new MutableLiveData<>();
            notesLiveData.setValue(notes);
        }

        return notesLiveData;
    }

    @Override
    public void addNoteToDB(Note note) {

    }

    @Override
    public void updateNoteInDB(Note note) {

    }

    @Override
    public void removeNoteFromDB(Note note) {

    }

    @Override
    public void removeAllNotesFromDB() {

    }
}
