package pan.alexander.pictureoftheday.framework.ui.viewpager.mars

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import pan.alexander.pictureoftheday.framework.App

private const val DEFAULT_SOL = 3000

class MarsViewModel : ViewModel() {
    private val marsInteractor = App.instance.daggerComponent.getMarsInteractor()

    private val marsPhotoLiveData by lazy { MutableLiveData<MarsActionData>() }

    fun getMarsPhotoLivaData(): LiveData<MarsActionData> = marsPhotoLiveData

    fun requestCuriosityPhoto(sol: Int = DEFAULT_SOL) = viewModelScope.launch {
        marsInteractor.get().getCuriosityPhotoBySol(sol)
            .onStart { marsPhotoLiveData.value = MarsActionData.Loading }
            .catch { marsPhotoLiveData.value = MarsActionData.Error(it) }
            .collect { marsPhotoLiveData.value = MarsActionData.Success(it) }
    }
}
