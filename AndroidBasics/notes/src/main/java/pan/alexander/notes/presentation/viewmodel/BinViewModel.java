package pan.alexander.notes.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BinViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BinViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is trash can fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
