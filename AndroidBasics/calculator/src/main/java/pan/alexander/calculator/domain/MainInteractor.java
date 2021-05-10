package pan.alexander.calculator.domain;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import pan.alexander.calculator.App;
import pan.alexander.calculator.domain.entities.HistoryData;

import static pan.alexander.calculator.App.LOG_TAG;
import static pan.alexander.calculator.util.AppConstants.DELAY_BEFORE_CLEAR_DISPOSABLES;
import static pan.alexander.calculator.util.AppConstants.MAX_HISTORY_ENTRIES;

public class MainInteractor {

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final HistoryRepository historyRepository;
    private final SettingsRepository settingsRepository;
    private final Calculator calculator;
    private final Handler globalHandler;

    @Inject
    public MainInteractor(HistoryRepository historyRepository, SettingsRepository settingsRepository, Calculator calculator) {
        this.historyRepository = historyRepository;
        this.settingsRepository = settingsRepository;
        this.calculator = calculator;
        this.globalHandler = App.getInstance().getHandler();
    }

    public String calculateExpression(String expression) {
        return calculator.calculateExpression(expression);
    }

    @NonNull
    public HistoryData getLastHistory() {
        HistoryData lastHistoryEntry = historyRepository.getLastHistoryEntry();
        return lastHistoryEntry != null ? lastHistoryEntry : new HistoryData("", "", System.currentTimeMillis());
    }

    public void saveHistory(HistoryData historyData) {
        disposables.add(historyRepository.insertHistory(historyData)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        this::trimHistorySize,
                        throwable -> Log.e(LOG_TAG, "MainInteractor saveHistory exception " + throwable.getMessage()))
        );
    }

    private void trimHistorySize() {
        historyRepository.clearEmptyEntries();

        int historySize = historyRepository.getHistoryEntriesCount();

        if (historySize > MAX_HISTORY_ENTRIES) {
            historyRepository.deleteFirstHistoryEntries(historySize - MAX_HISTORY_ENTRIES);
        }
    }

    public String getStringPreference(String key) {
        return settingsRepository.getSettings(key);
    }

    public LiveData<List<HistoryData>> getHistory() {
        return historyRepository.getHistory();
    }

    public Completable deleteHistoryEntry(HistoryData historyData) {
        return historyRepository.deleteHistory(historyData);
    }

    public void clearDisposablesDelayed() {
        if (globalHandler != null) {
            globalHandler.postDelayed(disposables::clear, DELAY_BEFORE_CLEAR_DISPOSABLES);
        }
    }
}
