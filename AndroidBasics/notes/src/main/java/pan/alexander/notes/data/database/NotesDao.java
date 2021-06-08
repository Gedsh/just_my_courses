package pan.alexander.notes.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import pan.alexander.notes.domain.entities.Note;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface NotesDao {
    @Query("SELECT * FROM Note")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM Note WHERE time = :timestamp LIMIT 1")
    Single<List<Note>> getNoteByTime(long timestamp);

    @Insert(onConflict = REPLACE)
    Completable insertNote(Note note);

    @Insert(onConflict = REPLACE)
    Completable insertNotes(List<Note> notes);

    @Update(onConflict = REPLACE)
    Completable updateNote(Note note);

    @Delete
    Completable deleteNote(Note note);

    @Delete
    Completable deleteNotes(List<Note> notes);

    @Query("DELETE FROM Note WHERE id IN (SELECT id FROM Note WHERE " +
            "(title = '' AND description = '') " +
            "AND id IN " +
            "(SELECT id FROM Note ORDER BY time DESC LIMIT " +
            "(SELECT COUNT(id) FROM Note) OFFSET 1))")
    Completable deleteEmptyNotesExceptLast();

    @Query("DELETE FROM Note")
    Completable deleteAllNotes();
}
