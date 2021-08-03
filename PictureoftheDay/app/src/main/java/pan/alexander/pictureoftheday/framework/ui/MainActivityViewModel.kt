package pan.alexander.pictureoftheday.framework.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import pan.alexander.pictureoftheday.domain.settings.AppTheme
import pan.alexander.pictureoftheday.framework.App

class MainActivityViewModel : ViewModel() {

    private val errorMessageLiveData by lazy { MutableLiveData<String>() }

    private val settingRepository = App.instance.daggerComponent.getSettingsRepository()

    fun getErrorMessageLiveData(): LiveData<String> = errorMessageLiveData

    fun showErrorMessage(message: String) {
        errorMessageLiveData.value = message
    }

    fun getAppTheme() = settingRepository.get().getAppTheme()

    fun getAppUiMode() = settingRepository.get().getAppUiMode()

    fun getSettingsChangedLiveData() = settingRepository.get().getSettingsChangedLiveData()

    var currentTheme = getAppTheme()
    var currentUiMode = getAppUiMode()

    var isMain = true
}
