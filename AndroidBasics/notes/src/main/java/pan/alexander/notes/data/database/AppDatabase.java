package pan.alexander.notes.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import pan.alexander.notes.domain.entities.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NotesDao getNotesDao();
}
