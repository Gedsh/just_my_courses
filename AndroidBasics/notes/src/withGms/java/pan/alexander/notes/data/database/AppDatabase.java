package pan.alexander.notes.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import pan.alexander.notes.domain.entities.Note;
import pan.alexander.notes.domain.entities.Trash;

@Database(entities = {Trash.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TrashDao getTrashDao();
}
