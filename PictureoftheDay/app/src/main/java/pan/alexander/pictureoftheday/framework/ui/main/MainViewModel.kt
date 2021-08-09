package pan.alexander.pictureoftheday.framework.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import pan.alexander.pictureoftheday.framework.App
import java.util.*

class MainViewModel : ViewModel() {

    private val pictureInteractor = App.instance.daggerComponent.getMainInteractor()

    private val pictureLiveData by lazy { MutableLiveData<PictureActionData>() }

    fun getPictureLiveData(): LiveData<PictureActionData> = pictureLiveData

    fun requestPictureOfTheDay(date: Date) = viewModelScope.launch {
        pictureInteractor.get().getPhotoByDate(date)
            .onStart { pictureLiveData.value = PictureActionData.Loading }
            .catch { pictureLiveData.value = PictureActionData.Error(it) }
            .collect { pictureLiveData.value = PictureActionData.Success(it) }
    }
}
