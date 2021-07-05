package pan.alexander.notes.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.domain.entities.Trash;

@Database(entities = {Note.class, Trash.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NotesDao getNotesDao();

    public abstract TrashDao getTrashDao();
}
