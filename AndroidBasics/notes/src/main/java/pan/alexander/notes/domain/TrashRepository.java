package pan.alexander.notes.domain;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.domain.entities.Trash;

public interface TrashRepository {
    LiveData<List<Note>> getAllTrashesFromDB();

    Completable addTrashToDB(Trash trash);

    Completable addTrashesToDB(List<Trash> trashes);

    Completable removeTrashFromDB(Trash trash);

    Completable removeTrashesFromDB(List<Trash> trashes);

    Single<Integer> getTrashSize();

    Completable trimTrashToSize(int trashCapacity);

    Completable removeAllTrashesFromDB();
}
