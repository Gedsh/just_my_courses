package pan.alexander.calculator.domain;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Completable;
import pan.alexander.calculator.domain.entities.HistoryData;

public interface HistoryRepository {
    LiveData<List<HistoryData>> getHistory();
    HistoryData getLastHistoryEntry();
    int getHistoryEntriesCount();
    Completable clearHistory();
    void clearEmptyEntries();
    void deleteFirstHistoryEntries(int entriesQuantity);
    Completable insertHistory(HistoryData historyData);
    Completable deleteHistory(HistoryData historyData);
}
