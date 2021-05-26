package pan.alexander.notes.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pan.alexander.notes.domain.entities.Note;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface NotesDao {
    @Query("SELECT * FROM Note")
    LiveData<List<Note>> getAllNotes();

    @Insert(onConflict = REPLACE)
    void insertNote(Note note);

    @Update(onConflict = REPLACE)
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("DELETE FROM Note")
    void deleteAllNotes();
}
