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
        for (int i = 0; i < 1000; i += 100) {
            add(new Note("Monday", "Wake up", NoteType.TEXT_NOTE, System.currentTimeMillis() + i * 10000, "#FFFFE57F"));
            add(new Note("Tuesday", "Ready", NoteType.TEXT_NOTE, System.currentTimeMillis() + i * 20000, "#d3ff99"));
            add(new Note("Wednesday", "Work", NoteType.TEXT_NOTE, System.currentTimeMillis() + i * 30000, "#FFFFE57F"));
            add(new Note("Thursday", "Tired", NoteType.TEXT_NOTE, System.currentTimeMillis() + i * 40000, "#d3ff99"));
            add(new Note("Friday", "Wait", NoteType.TEXT_NOTE, System.currentTimeMillis() + i * 50000, "#FFFFE57F"));
            add(new Note("Saturday", "Play", NoteType.TEXT_NOTE, System.currentTimeMillis() + i * 60000, "#ffa099"));
            add(new Note("Sunday", "Sleep", NoteType.TEXT_NOTE, System.currentTimeMillis() + i * 70000, "#99f0ff"));
        }
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
