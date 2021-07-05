package pan.alexander.notes.presentation.viewmodel;

import android.os.Handler;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import dagger.Lazy;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pan.alexander.notes.App;
import pan.alexander.notes.domain.AccountInteractor;
import pan.alexander.notes.domain.MainInteractor;
import pan.alexander.notes.domain.account.User;
import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.presentation.activities.MainActivityActionRequest;

import static pan.alexander.notes.App.LOG_TAG;
import static pan.alexander.notes.utils.AppConstants.DELAY_BEFORE_STOP_RX_SCHEDULERS;

public class MainActivityViewModel extends ViewModel {
    public static final int DEFAULT_BOTTOM_NAVIGATION_VIEW_HEIGHT = 0;

    private final CompositeDisposable disposables;
    private MutableLiveData<Integer> bottomNavigationViewShowed;
    private MutableLiveData<Boolean> keyboardActivated;
    private Pair<Integer, Note> currentNote;
    private MutableLiveData<Pair<Integer, Note>> displayedNoteCallbackForSave;
    private MutableLiveData<Integer> mainActivityActionRequest;
    private final Handler globalHandler;
    private MutableLiveData<User> userAccountLiveData;
    private List<Note> notesFromAnonymousAccount;
    private final Lazy<MainInteractor> mainInteractor;
    private final Lazy<AccountInteractor> accountInteractor;

    public MainActivityViewModel() {
        globalHandler = App.getInstance().getHandler();
        mainInteractor = App.getInstance().getDaggerComponent().getMainInteractor();
        accountInteractor = App.getInstance().getDaggerComponent().getAccountInteractor();
        disposables = new CompositeDisposable();
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

    @NonNull
    public LiveData<User> getUserAccountLiveData() {
        if (userAccountLiveData == null) {
            updateUser();
        }
        return userAccountLiveData;
    }

    public void updateUser() {
        if (userAccountLiveData == null) {
            userAccountLiveData = new MutableLiveData<>();
        }
        User user = accountInteractor.get().getUser();
        userAccountLiveData.setValue(user);
        accountInteractor.get().setUser(user);
    }

    private void clearUser() {
        userAccountLiveData.setValue(null);
        accountInteractor.get().setUser(null);
    }

    public void clearNotesFromAnonymousAccount() {
        this.notesFromAnonymousAccount = null;
    }

    public LiveData<Integer> getMainActivityActionRequest() {
        if (mainActivityActionRequest == null) {
            mainActivityActionRequest = new MutableLiveData<>();
            mainActivityActionRequest.setValue(MainActivityActionRequest.NO_ACTION);
        }
        return mainActivityActionRequest;
    }

    public void clearMainActivityActionRequest() {
        if (mainActivityActionRequest.getValue() != null
                && mainActivityActionRequest.getValue() != MainActivityActionRequest.NO_ACTION) {
            mainActivityActionRequest.setValue(MainActivityActionRequest.NO_ACTION);
        }
    }

    private void setMainActivityActionRequest(@MainActivityActionRequest int action) {
        if (mainActivityActionRequest == null) {
            mainActivityActionRequest = new MutableLiveData<>();
        }
        mainActivityActionRequest.setValue(action);
    }

    public void moveUserDataFromAnonymousToRegisteredAccount() {
        List<Note> notes = mainInteractor.get().getAllNotesFromNotes().getValue();

        if (notes != null && !notes.isEmpty()) {
            notesFromAnonymousAccount = notes;
        }

        Disposable disposable = mainInteractor.get().removeAllNotesFromNotes()
                .andThen(accountInteractor.get().signOut())
                .andThen(accountInteractor.get().deleteCurrentUser())
                .subscribe(() -> {
                            setMainActivityActionRequest(MainActivityActionRequest.LAUNCH_ACCOUNT_CHOOSER);
                            clearUser();
                        },
                        throwable -> Log.e(LOG_TAG, "User delete exception", throwable));
        disposables.add(disposable);
    }

    public void getUserAccountError(Pair<String, Integer> result) {

        clearUser();

        if (notesFromAnonymousAccount != null && !notesFromAnonymousAccount.isEmpty()) {
            Disposable disposable = accountInteractor.get()
                    .signInAnonymously()
                    .subscribe(() -> {
                                mainInteractor.get().addNotesToNotes(notesFromAnonymousAccount);
                                clearNotesFromAnonymousAccount();
                                updateUser();
                                setMainActivityActionRequest(MainActivityActionRequest.OPEN_NAV_DRAWER);
                            },
                            throwable -> Log.e(LOG_TAG, "User sign in anonymously exception", throwable));
            disposables.add(disposable);
        }

        Log.e(LOG_TAG, "Failed to get user account " + result);
    }

    public void getUserAccountSuccess() {
        updateUser();

        if (notesFromAnonymousAccount != null && !notesFromAnonymousAccount.isEmpty()) {
            mainInteractor.get().addNotesToNotes(notesFromAnonymousAccount);
            clearNotesFromAnonymousAccount();
        }
    }

    public void signOutUser() {
        Disposable disposable = accountInteractor.get()
                .signOut()
                .subscribe(() -> {
                            clearUser();
                            setMainActivityActionRequest(MainActivityActionRequest.OPEN_NAV_DRAWER);
                        },
                        throwable -> Log.e(LOG_TAG, "User sign out exception", throwable));
        disposables.add(disposable);
    }

    @Override
    protected void onCleared() {

        stopRxSchedulersDelayed();

        disposables.clear();
        mainInteractor.get().clearDisposablesDelayed();

        super.onCleared();
    }
}
