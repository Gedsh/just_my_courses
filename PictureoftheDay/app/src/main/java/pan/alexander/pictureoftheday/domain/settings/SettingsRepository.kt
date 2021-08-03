package pan.alexander.pictureoftheday.domain.settings

import androidx.lifecycle.LiveData

interface SettingsRepository {
    fun getAppTheme(): AppTheme
    fun setAppTheme(appTheme: AppTheme)
    fun getAppUiMode(): AppUiMode
    fun setAppUiMode(appUiMode: AppUiMode)
    fun getSettingsChangedLiveData(): LiveData<SettingsAction>
}
