package pan.alexander.notes.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.domain.entities.Trash;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TrashDao {
    @Query("SELECT * FROM Trash")
    LiveData<List<Note>> getAllTrashes();

    @Insert(onConflict = REPLACE)
    Completable insertTrashes(List<Trash> trashes);

    @Insert(onConflict = REPLACE)
    Completable insertTrash(Trash trash);

    @Delete
    Completable deleteTrash(Trash trash);

    @Delete
    Completable deleteTrashes(List<Trash> trashes);

    @Query("SELECT COUNT(id) FROM Trash")
    Single<Integer> getTrashSize();

    @Query("DELETE FROM Trash WHERE id IN " +
            "(SELECT id FROM Trash ORDER BY time DESC LIMIT " +
            "(SELECT COUNT(id) FROM Trash) OFFSET :trashCapacity)")
    Completable trimTrash(int trashCapacity);

    @Query("DELETE FROM Trash")
    Completable deleteAllTrashes();
}
