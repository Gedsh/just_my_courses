package pan.alexander.calculator.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import pan.alexander.calculator.domain.entities.HistoryData;

@Database(entities = {HistoryData.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HistoryDao historyDao();
}
