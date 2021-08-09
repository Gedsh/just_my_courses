package pan.alexander.pictureoftheday.framework.ui.viewpager.earth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import pan.alexander.pictureoftheday.framework.App

class EarthViewModel : ViewModel() {
    private val epicInteractor = App.instance.daggerComponent.getEpicInteractor()

    private val epicLiveData by lazy { MutableLiveData<EpicActionData>() }

    fun getEpicLivaData(): LiveData<EpicActionData> = epicLiveData

    fun requestEpicRecentEnhancedColorImage() = viewModelScope.launch {
        epicInteractor.get().getRecentEnhancedColorImage()
            .onStart { epicLiveData.value = EpicActionData.Loading }
            .catch { epicLiveData.value = EpicActionData.Error(it) }
            .collect { epicLiveData.value = EpicActionData.Success(it) }
    }
}
