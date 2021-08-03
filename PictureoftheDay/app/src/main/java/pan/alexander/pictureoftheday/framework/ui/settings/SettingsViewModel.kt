package pan.alexander.pictureoftheday.framework.ui.settings

import androidx.lifecycle.ViewModel
import pan.alexander.pictureoftheday.domain.settings.AppUiMode
import pan.alexander.pictureoftheday.domain.settings.AppTheme
import pan.alexander.pictureoftheday.framework.App

class SettingsViewModel : ViewModel() {
    private val settingRepository = App.instance.daggerComponent.getSettingsRepository()

    fun getAppTheme() = settingRepository.get().getAppTheme()
    fun getAppUiMode() = settingRepository.get().getAppUiMode()

    fun setAppTheme(appTheme: AppTheme) = settingRepository.get().setAppTheme(appTheme)
    fun setAppMode(appUiMode: AppUiMode) = settingRepository.get().setAppUiMode(appUiMode)
}
