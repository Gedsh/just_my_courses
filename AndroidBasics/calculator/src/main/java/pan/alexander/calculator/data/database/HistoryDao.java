package pan.alexander.calculator.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import pan.alexander.calculator.domain.entities.HistoryData;

@Dao
public interface HistoryDao {
    @Query("SELECT * From HistoryData WHERE result != '' ORDER BY time DESC")
    LiveData<List<HistoryData>> getHistoryData();

    @Query("SELECT * FROM HistoryData ORDER BY time DESC LIMIT 1")
    HistoryData getLastHistoryEntry();

    @Query("SELECT COUNT(time) FROM HistoryData")
    int getHistoryEntriesCount();

    @Query("DELETE FROM HistoryData")
    Completable clearHistoryData();

    @Query("DELETE FROM HistoryData WHERE time IN (SELECT time FROM HistoryData WHERE (expression = '' OR result = '') AND time IN (SELECT time FROM HistoryData ORDER BY TIME DESC LIMIT (SELECT COUNT(time) FROM HistoryData) OFFSET 1))")
    void clearEmptyEntries();

    @Query("DELETE FROM HistoryData WHERE time IN (SELECT time FROM HistoryData ORDER BY time LIMIT :entriesQuantity)")
    void deleteFirstEntries(int entriesQuantity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertHistoryEntry(HistoryData historyData);

    @Delete
    Completable deleteEntry(HistoryData historyData);
}
