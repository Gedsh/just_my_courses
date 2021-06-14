package pan.alexander.notes.domain;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pan.alexander.notes.App;
import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.domain.entities.Trash;
import pan.alexander.notes.utils.NoteRoomDataMapping;

import static pan.alexander.notes.App.LOG_TAG;
import static pan.alexander.notes.utils.AppConstants.DEFAULT_TRASH_CAPACITY;
import static pan.alexander.notes.utils.AppConstants.DELAY_BEFORE_CLEAR_DISPOSABLES;

@Singleton
public class MainInteractor {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final NotesRepository notesRepository;
    private final TrashRepository trashRepository;
    private final Handler globalHandler;

    @Inject
    public MainInteractor(NotesRepository notesRepository, TrashRepository trashRepository) {
        this.notesRepository = notesRepository;
        this.trashRepository = trashRepository;
        this.globalHandler = App.getInstance().getHandler();
    }

    public LiveData<List<Note>> getAllNotesFromNotes() {
        return notesRepository.getAllNotesFromDB();
    }

    public void addNoteToNotes(Note note) {
        Disposable disposable = notesRepository.addNoteToDB(note)
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                }, throwable -> Log.e(LOG_TAG, "Add note exception " + note, throwable));
        disposables.add(disposable);
    }

    public void addNotesToNotes(List<Note> notes) {
        Disposable disposable = notesRepository.addNotesToDB(notes)
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                }, throwable -> Log.e(LOG_TAG, "Add note exception " + notes, throwable));
        disposables.add(disposable);
    }

    public void updateNoteInNotes(Note note) {
        Disposable disposable = notesRepository.updateNoteInDB(note)
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                }, throwable -> Log.e(LOG_TAG, "Update note exception " + note, throwable));
        disposables.add(disposable);
    }

    public void moveNoteFromNotesToTrash(Note note) {
        Disposable disposable = trashRepository.addTrashToDB(NoteRoomDataMapping.noteToTrash(note))
                .andThen(notesRepository.removeNoteFromDB(note))
                .observeOn(Schedulers.io())
                .andThen(trashRepository.getTrashSize())
                .flatMapCompletable(size -> {
                    if (size > DEFAULT_TRASH_CAPACITY) {
                        return trashRepository.trimTrashToSize(DEFAULT_TRASH_CAPACITY);
                    }
                    return CompletableObserver::onComplete;
                })
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                }, throwable -> Log.e(LOG_TAG, "Move note to trash exception " + note, throwable));
        disposables.add(disposable);
    }

    public void removeNoteFromNotes(Note note) {
        Disposable disposable = notesRepository.removeNoteFromDB(note)
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                }, throwable -> Log.e(LOG_TAG, "Remove note exception " + note, throwable));
        disposables.add(disposable);
    }

    public void removeEmptyNotesFromNotesExceptLast() {
        Disposable disposable = notesRepository.removeEmptyNotesFromDBExceptLast()
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                }, throwable -> Log.e(LOG_TAG, "Remove empty notes exception", throwable));
        disposables.add(disposable);
    }

    public void moveNotesFromNotesToTrash(List<Note> notes) {
        Disposable disposable = trashRepository.addTrashesToDB(NoteRoomDataMapping.notesToTrashes(notes))
                .andThen(notesRepository.removeNotesFromDB(notes))
                .observeOn(Schedulers.io())
                .andThen(trashRepository.getTrashSize())
                .flatMapCompletable(size -> {
                    if (size > DEFAULT_TRASH_CAPACITY) {
                        return trashRepository.trimTrashToSize(DEFAULT_TRASH_CAPACITY);
                    }
                    return CompletableObserver::onComplete;
                })
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                }, throwable -> Log.e(LOG_TAG, "Move note to trash exception " + notes, throwable));
        disposables.add(disposable);
    }

    public void removeNotesFromNotes(List<Note> notes) {
        Disposable disposable = notesRepository.removeNotesFromDB(notes)
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                }, throwable -> Log.e(LOG_TAG, "Remove notes exception " + notes, throwable));
        disposables.add(disposable);
    }

    public Completable removeAllNotesFromNotes() {
        return notesRepository.removeAllNotesFromDB();
    }

    public void moveTrashFromTrashToNotes(Trash trash) {
        Disposable disposable = notesRepository.addNoteToDB(NoteRoomDataMapping.trashToNote(trash))
                .observeOn(Schedulers.io())
                .andThen(trashRepository.removeTrashFromDB(trash))
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                }, throwable -> Log.e(LOG_TAG, "Move note to trash exception " + trash, throwable));
        disposables.add(disposable);
    }

    public void moveTrashesFromTrashToNotes(List<Trash> trashes) {
        Disposable disposable = notesRepository.addNotesToDB(NoteRoomDataMapping.trashesToNotes(trashes))
                .observeOn(Schedulers.io())
                .andThen(trashRepository.removeTrashesFromDB(trashes))
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                }, throwable -> Log.e(LOG_TAG, "Move note to trash exception " + trashes, throwable));
        disposables.add(disposable);
    }

    public void clearTrash() {
        trashRepository.removeAllTrashesFromDB();
    }

    public void clearDisposablesDelayed() {
        if (globalHandler != null) {
            globalHandler.postDelayed(disposables::clear, DELAY_BEFORE_CLEAR_DISPOSABLES);
        }
    }
}
