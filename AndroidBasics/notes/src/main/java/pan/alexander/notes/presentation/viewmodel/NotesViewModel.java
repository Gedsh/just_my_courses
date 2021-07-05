package pan.alexander.notes.presentation.viewmodel;

import android.util.Log;
import android.util.SparseBooleanArray;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import dagger.Lazy;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import pan.alexander.notes.App;
import pan.alexander.notes.domain.AccountInteractor;
import pan.alexander.notes.domain.MainInteractor;
import pan.alexander.notes.domain.entities.Note;

import static pan.alexander.notes.App.LOG_TAG;

public class NotesViewModel extends ViewModel {

    private final Lazy<MainInteractor> mainInteractor;
    private final Lazy<AccountInteractor> accountInteractor;
    private final CompositeDisposable disposables;
    private LiveData<List<Note>> notesLiveData;
    private SparseBooleanArray selectedNotesIndices;

    public NotesViewModel() {
        mainInteractor = App.getInstance().getDaggerComponent().getMainInteractor();
        accountInteractor = App.getInstance().getDaggerComponent().getAccountInteractor();
        disposables = new CompositeDisposable();
    }

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

    public void requestNewNote(Note note) {
        mainInteractor.get().addNoteToNotes(note);
    }

    public void signInAnonymously(Note note, MainActivityViewModel mainActivityViewModel) {
        Disposable disposable = accountInteractor.get()
                .signInAnonymously()
                .subscribe(() -> {
                            mainActivityViewModel.updateUser();
                            mainInteractor.get().addNoteToNotes(note);
                        },
                        throwable -> Log.e(LOG_TAG, "User sign in anonymously exception", throwable));
        disposables.add(disposable);
    }

    @Override
    protected void onCleared() {
        disposables.clear();

        super.onCleared();
    }
}
