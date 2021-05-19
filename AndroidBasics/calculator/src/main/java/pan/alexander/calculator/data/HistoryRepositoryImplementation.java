package pan.alexander.calculator.data;

import androidx.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import pan.alexander.calculator.App;
import pan.alexander.calculator.data.database.HistoryDao;
import pan.alexander.calculator.domain.HistoryRepository;
import pan.alexander.calculator.domain.entities.HistoryData;

public class HistoryRepositoryImplementation implements HistoryRepository {
    private final HistoryDao dao = App.getInstance().getDaggerComponent().getHistoryDao();

    @Inject
    public HistoryRepositoryImplementation() {
    }

    @Override
    public LiveData<List<HistoryData>> getHistory() {
        return dao.getHistoryData();
    }

    @Override
    public HistoryData getLastHistoryEntry() {
        return dao.getLastHistoryEntry();
    }

    @Override
    public int getHistoryEntriesCount() {
        return dao.getHistoryEntriesCount();
    }

    @Override
    public Completable clearHistory() {
        return dao.clearHistoryData();
    }

    @Override
    public void clearEmptyEntries() {
        dao.clearEmptyEntries();
    }

    @Override
    public void deleteFirstHistoryEntries(int entriesQuantity) {
        dao.deleteFirstEntries(entriesQuantity);
    }

    @Override
    public Completable insertHistory(HistoryData historyData) {
        return dao.insertHistoryEntry(historyData);
    }

    @Override
    public Completable deleteHistory(HistoryData historyData) {
        return dao.deleteEntry(historyData);
    }
}
