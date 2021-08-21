package pan.alexander.pictureoftheday.framework.ui

import androidx.lifecycle.ViewModel
import pan.alexander.pictureoftheday.framework.App

class SplashActivityViewModel : ViewModel() {
    private val settingRepository = App.instance.daggerComponent.getSettingsRepository()

    fun getAppTheme() = settingRepository.get().getAppTheme()

    fun getAppUiMode() = settingRepository.get().getAppUiMode()
}
