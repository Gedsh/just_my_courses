package pan.alexander.notes.presentation.viewmodel;

import android.util.SparseBooleanArray;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import pan.alexander.notes.App;
import pan.alexander.notes.domain.entities.Note;

public class NotesViewModel extends ViewModel {

    private LiveData<List<Note>> notesLiveData;
    private SparseBooleanArray selectedNotesIndices;

    public LiveData<List<Note>> getNotesLiveData() {
        if (notesLiveData == null) {
            notesLiveData = App.getInstance().getDaggerComponent().getMainInteractor().get().getAllNotesFromNotes();
        }
        return notesLiveData;
    }

    public SparseBooleanArray getSelectedNotesIndices() {
        if (selectedNotesIndices == null) {
            selectedNotesIndices = new SparseBooleanArray();
        }
        return selectedNotesIndices;
    }
}
