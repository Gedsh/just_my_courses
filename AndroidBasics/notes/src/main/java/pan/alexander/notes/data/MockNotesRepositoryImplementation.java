package pan.alexander.notes.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import pan.alexander.notes.domain.NotesRepository;
import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.domain.entities.NoteType;
import pan.alexander.notes.domain.tasksnote.Task;

public class MockNotesRepositoryImplementation implements NotesRepository {
    private MutableLiveData<List<Note>> notesLiveData;

    private final List<Task> task = new ArrayList<Task>() {{
        add(new Task("Plan your morning at night.", 0));
        add(new Task("Set your wake up time.", 1));
        add(new Task("Choose an alarm you can live with.", 2));
        add(new Task("Drink a glass of water.", 3));
        add(new Task("Make your bed.", 4));
        add(new Task("Get moving.", 5));
        add(new Task("Stay unplugged.", 6));
        add(new Task("Sneak in a little me-time.", 7));
    }};

    private final List<Note> notes = new ArrayList<Note>() {{
        final String tasks = new Gson().toJson(task, new TypeToken<Collection<Task>>() {
        }.getType());
        for (int i = 0; i < 1000; i += 100) {
            add(new Note("Monday", tasks, NoteType.LIST_NOTE, System.currentTimeMillis() + i * 10000, "#FFE57F"));
            add(new Note("Tuesday", "Ready", NoteType.TEXT_NOTE, System.currentTimeMillis() + i * 20000, "#d3ff99"));
            add(new Note("Wednesday", "Work", NoteType.TEXT_NOTE, System.currentTimeMillis() + i * 30000, "#FFE57F"));
            add(new Note("Thursday", "Tired", NoteType.TEXT_NOTE, System.currentTimeMillis() + i * 40000, "#d3ff99"));
            add(new Note("Friday", "Wait", NoteType.TEXT_NOTE, System.currentTimeMillis() + i * 50000, "#FFE57F"));
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
    public Single<List<Note>> getNoteByTime(long time) {
        return null;
    }

    @Override
    public Completable addNoteToDB(Note note) {
        return null;
    }

    @Override
    public Completable addNotesToDB(List<Note> notes) {
        return null;
    }

    @Override
    public Completable updateNoteInDB(Note note) {
        return null;
    }

    @Override
    public Completable removeNoteFromDB(Note note) {
        return null;
    }

    @Override
    public Completable removeEmptyNotesFromDBExceptLast() {
        return null;
    }

    @Override
    public Completable removeNotesFromDB(List<Note> notes) {
        return null;
    }

    @Override
    public Completable removeAllNotesFromDB() {
        return null;
    }
}
