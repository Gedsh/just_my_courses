package pan.alexander.pictureoftheday.framework.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    private val errorMessageLiveData by lazy { MutableLiveData<String>() }

    fun getErrorMessageLiveData(): LiveData<String> = errorMessageLiveData

    fun showErrorMessage(message: String) {
        errorMessageLiveData.value = message
    }

    var isMain = true
}
