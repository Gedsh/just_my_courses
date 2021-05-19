package pan.alexander.calculator.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import pan.alexander.calculator.App;
import pan.alexander.calculator.domain.entities.HistoryData;

public class HistoryViewModel extends ViewModel {

    private LiveData<List<HistoryData>> historyLiveData;

    public LiveData<List<HistoryData>> getHistoryLiveData() {
        if (historyLiveData == null) {
           historyLiveData = App.getInstance().getDaggerComponent().getMainInteractor().getHistory();
        }
        return historyLiveData;
    }
}
