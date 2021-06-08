package pan.alexander.notes.presentation.viewmodel;

import android.os.Handler;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.schedulers.Schedulers;
import pan.alexander.notes.App;
import pan.alexander.notes.domain.entities.Note;

import static pan.alexander.notes.utils.AppConstants.DELAY_BEFORE_STOP_RX_SCHEDULERS;

public class MainActivityViewModel extends ViewModel {
    public static final int DEFAULT_BOTTOM_NAVIGATION_VIEW_HEIGHT = 0;

    private MutableLiveData<Integer> bottomNavigationViewShowed;
    private MutableLiveData<Boolean> keyboardActivated;
    private Pair<Integer, Note> currentNote;
    private MutableLiveData<Pair<Integer, Note>> displayedNoteCallbackForSave;
    private final Handler globalHandler;

    public MainActivityViewModel() {
        this.globalHandler = App.getInstance().getHandler();
        stopPreviousGlobalHandlerTasks();
        startRxSchedulers();
    }

    private void stopPreviousGlobalHandlerTasks() {
        if (globalHandler != null) {
            globalHandler.removeCallbacksAndMessages(null);
        }
    }

    private void startRxSchedulers() {
        Schedulers.start();
    }

    @NonNull
    public LiveData<Integer> getBottomNavigationViewShowed() {
        if (bottomNavigationViewShowed == null) {
            bottomNavigationViewShowed = new MutableLiveData<>();
            bottomNavigationViewShowed.setValue(DEFAULT_BOTTOM_NAVIGATION_VIEW_HEIGHT);
        }

        return bottomNavigationViewShowed;
    }

    public void setBottomNavigationViewShowed(int bottomAppBarHeight) {
        if (bottomNavigationViewShowed == null) {
            bottomNavigationViewShowed = new MutableLiveData<>();
        }
        bottomNavigationViewShowed.setValue(bottomAppBarHeight);
    }

    public LiveData<Boolean> getKeyboardActivated() {
        if (keyboardActivated == null) {
            keyboardActivated = new MutableLiveData<>();
            keyboardActivated.setValue(false);
        }
        return keyboardActivated;
    }

    public void setKeyboardActivated(boolean activated) {
        if (keyboardActivated == null) {
            keyboardActivated = new MutableLiveData<>();
        }
        keyboardActivated.setValue(activated);
    }

    @Nullable
    public Pair<Integer, Note> getCurrentNote() {
        return currentNote;
    }

    public void setCurrentNote(int position, @NonNull Note note) {
        this.currentNote = new Pair<>(position, note);
    }

    public void clearCurrentNote() {
        currentNote = null;
    }

    public LiveData<Pair<Integer, Note>> getDisplayedNoteCallbackLiveData() {
        if (displayedNoteCallbackForSave == null) {
            displayedNoteCallbackForSave = new MutableLiveData<>();
        }
        return displayedNoteCallbackForSave;
    }

    public void setDisplayedNoteCallbackLiveData(Pair<Integer, Note> displayedNotePositionToNote) {
        if (displayedNoteCallbackForSave == null) {
            displayedNoteCallbackForSave = new MutableLiveData<>();
        }
        displayedNoteCallbackForSave.setValue(displayedNotePositionToNote);
    }

    public void stopRxSchedulersDelayed() {
        if (globalHandler != null) {
            globalHandler.postDelayed(Schedulers::shutdown, DELAY_BEFORE_STOP_RX_SCHEDULERS);
        }
    }

    @Override
    protected void onCleared() {

        stopRxSchedulersDelayed();

        App.getInstance().getDaggerComponent().getMainInteractor().get().clearDisposablesDelayed();

        super.onCleared();
    }
}
