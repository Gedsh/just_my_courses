package pan.alexander.notes.data.database;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pan.alexander.notes.domain.account.User;
import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.utils.NoteFirestoreDataMapping;

import static pan.alexander.notes.App.LOG_TAG;

@Singleton
public class NotesDao {
    private final static String USERS_COLLECTION = "users";
    private final static String NOTES_COLLECTION = "notes";

    private final FirebaseFirestore db;
    private User user;
    private MutableLiveData<List<Note>> notesLiveData;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public NotesDao(FirestoreDatabase db) {
        this.db = db.getFirestoreDatabase();
    }

    public void setUser(User user) {
        Log.e(LOG_TAG, "Notes dao set user " + user);
        this.user = user;

        observeDbChanges(getNotesCollectionReference());
    }

    public LiveData<List<Note>> getAllNotes() {

        if (notesLiveData == null) {
            notesLiveData = new MutableLiveData<>();
        }

        CollectionReference notesCollectionReference = getNotesCollectionReference();

        if (notesCollectionReference == null) {
            return notesLiveData;
        }

        observeDbChanges(notesCollectionReference);

        return notesLiveData;
    }

    private CollectionReference getNotesCollectionReference() {

        if (user == null) {
            return null;
        }

        return db.collection(USERS_COLLECTION)
                .document(user.getUid())
                .collection(NOTES_COLLECTION);
    }

    private DocumentReference getUserDocumentReference() {

        if (user == null) {
            return null;
        }

        return db.collection(USERS_COLLECTION)
                .document(user.getUid());
    }

    private void observeDbChanges(CollectionReference notesCollectionReference) {

        if (notesLiveData == null) {
            notesLiveData = new MutableLiveData<>();
        }

        if (notesCollectionReference == null) {
            notesLiveData.setValue(Collections.emptyList());
            return;
        }

        notesCollectionReference.addSnapshotListener((value, error) -> {
            if (error == null) {
                List<Note> notes = NoteFirestoreDataMapping.snapshotToNotes(value);
                notesLiveData.setValue(notes);
            } else {
                notesLiveData.setValue(Collections.emptyList());
                Log.e(LOG_TAG, "NotesDao observeDbChanges error.", error);
            }
        });
    }

    public Completable insertNote(Note note) {
        return Completable.create(emitter -> {

            compositeDisposable.clear();

            CollectionReference notesCollectionReference = getNotesCollectionReference();

            if (notesCollectionReference == null) {
                if (!emitter.isDisposed()) {
                    emitter.onError(new IllegalAccessError("Cannot insert item " +
                            "into firestore when user is not defined"));
                }
                return;
            }

            notesCollectionReference.add(NoteFirestoreDataMapping.noteToDocument(note))
                    .addOnSuccessListener(documentReference -> {
                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) {
                            emitter.onError(e);
                        }
                    });
        });
    }

    public Completable insertNotes(List<Note> notes) {
        return Completable.create(emitter -> {
            CollectionReference notesCollectionReference = getNotesCollectionReference();

            if (notesCollectionReference == null) {
                if (!emitter.isDisposed()) {
                    emitter.onError(new IllegalAccessError("Cannot insert items " +
                            "in firestore when user is not defined"));
                }
                return;
            }

            List<DocumentReference> documentReferences = new ArrayList<>();
            for (int i = 0; i < notes.size(); i++) {
                documentReferences.add(notesCollectionReference.document());
            }

            WriteBatch batch = db.batch();
            for (int i = 0; i < notes.size(); i++) {
                batch.set(documentReferences.get(i), NoteFirestoreDataMapping.noteToDocument(notes.get(i)));
            }

            batch.commit().addOnSuccessListener(documentReference -> {
                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) {
                            emitter.onError(e);
                        }
                    });
        });
    }

    public Completable updateNote(Note note) {
        return Completable.create(emitter -> {
            CollectionReference notesCollectionReference = getNotesCollectionReference();

            if (notesCollectionReference == null) {
                if (!emitter.isDisposed()) {
                    emitter.onError(new IllegalAccessError("Cannot update item " +
                            "in firestore when user is not defined"));
                }
                return;
            }

            notesCollectionReference.document(note.getId()).set(NoteFirestoreDataMapping.noteToDocument(note))
                    .addOnSuccessListener(documentReference -> {
                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) {
                            emitter.onError(e);
                        }
                    });
        });
    }

    public Completable deleteNote(Note note) {
        return Completable.create(emitter -> {
            CollectionReference notesCollectionReference = getNotesCollectionReference();

            if (notesCollectionReference == null) {
                if (!emitter.isDisposed()) {
                    emitter.onError(new IllegalAccessError("Cannot delete item " +
                            "in firestore when user is not defined"));
                }
                return;
            }

            notesCollectionReference.document(note.getId()).delete()
                    .addOnSuccessListener(documentReference -> {
                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) {
                            emitter.onError(e);
                        }
                    });
        });
    }

    public Completable deleteNotes(List<Note> notes) {
        return Completable.create(emitter -> {
            CollectionReference notesCollectionReference = getNotesCollectionReference();

            if (notesCollectionReference == null) {
                if (!emitter.isDisposed()) {
                    emitter.onError(new IllegalAccessError("Cannot delete items " +
                            "in firestore when user is not defined"));
                }
                return;
            }

            List<DocumentReference> documentReferences = new ArrayList<>();
            for (Note note : notes) {
                documentReferences.add(notesCollectionReference.document(note.getId()));
            }

            WriteBatch batch = db.batch();
            for (DocumentReference documentReference : documentReferences) {
                batch.delete(documentReference);
            }

            batch.commit().addOnSuccessListener(documentReference -> {
                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) {
                            emitter.onError(e);
                        }
                    });
        });
    }

    public Completable deleteEmptyNotesExceptLast() {
        return Completable.create(emitter -> {
            CollectionReference notesCollectionReference = getNotesCollectionReference();

            if (notesCollectionReference == null) {
                if (!emitter.isDisposed()) {
                    emitter.onError(new IllegalAccessError("Cannot delete empty items " +
                            "in firestore when user is not defined"));
                }
                return;
            }

            notesCollectionReference.whereEqualTo(FirestoreFields.TITLE, "")
                    .whereEqualTo(FirestoreFields.DESCRIPTION, "")
                    .orderBy(FirestoreFields.TIME, Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Note> emptyNotes = NoteFirestoreDataMapping.snapshotToNotes(queryDocumentSnapshots);

                        if (emptyNotes.size() < 1) {
                            if (!emitter.isDisposed()) {
                                emitter.onComplete();
                            }
                            return;
                        }

                        emptyNotes.remove(0);
                        Disposable disposable = deleteNotes(emptyNotes)
                                .subscribeOn(Schedulers.io())
                                .subscribe(() -> {
                                }, throwable -> {
                                    if (!emitter.isDisposed()) {
                                        emitter.onError(throwable);
                                    }
                                });
                        compositeDisposable.add(disposable);
                    }).addOnFailureListener(e -> {
                if (!emitter.isDisposed()) {
                    emitter.onError(e);
                }
            });
        });
    }

    public Completable deleteAllNotes() {
        return Completable.create(emitter -> {
            DocumentReference userDocumentReference = getUserDocumentReference();
            CollectionReference notesCollectionReference = getNotesCollectionReference();

            if (userDocumentReference == null || notesCollectionReference == null) {
                if (!emitter.isDisposed()) {
                    emitter.onError(new IllegalAccessError("Cannot delete all user items " +
                            "in firestore when user is not defined"));
                }
                return;
            }

            notesCollectionReference.get().addOnSuccessListener(queryDocumentSnapshots -> {
                List<Note> notes = NoteFirestoreDataMapping.snapshotToNotes(queryDocumentSnapshots);

                Disposable disposable = deleteNotes(notes)
                        .subscribeOn(Schedulers.io())
                        .subscribe(() -> {
                                    userDocumentReference.delete();
                                    if (!emitter.isDisposed()) {
                                        emitter.onComplete();
                                    }
                                },
                                throwable -> {
                                    if (!emitter.isDisposed()) {
                                        emitter.onError(throwable);
                                    }
                                });
                compositeDisposable.add(disposable);
            }).addOnFailureListener(e -> {
                if (!emitter.isDisposed()) {
                    emitter.onError(e);
                }
            });
        });
    }
}
