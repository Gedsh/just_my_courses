package pan.alexander.notes.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
    private final MutableLiveData<Integer> bottomNavigationViewShowed = new MutableLiveData<>();
    private final MutableLiveData<Boolean> keyboardActivated = new MutableLiveData<>();

    public LiveData<Integer> getBottomNavigationViewShowed() {
        return bottomNavigationViewShowed;
    }

    public void setBottomNavigationViewShowed(int bottomAppBarHeight) {
        bottomNavigationViewShowed.setValue(bottomAppBarHeight);
    }

    public LiveData<Boolean> getKeyboardActivated() {
        return keyboardActivated;
    }

    public void setKeyboardActivated(boolean activated) {
        keyboardActivated.setValue(activated);
    }
}
