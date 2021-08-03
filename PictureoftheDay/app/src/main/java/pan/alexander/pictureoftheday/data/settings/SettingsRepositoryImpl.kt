package pan.alexander.pictureoftheday.data.settings

import androidx.lifecycle.LiveData
import pan.alexander.pictureoftheday.domain.settings.*
import pan.alexander.pictureoftheday.framework.preferences.SettingsHelper
import javax.inject.Inject
import javax.inject.Singleton

private val DEFAULT_APP_THEME = AppTheme.PURPLE.orderNumber
private val DEFAULT_APP_MODE = AppUiMode.MODE_AUTO.orderNumber

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val settingsHelper: SettingsHelper
) : SettingsRepository {
    private val settingsChangedLiveData by lazy { SettingsChangedLiveData() }

    override fun getAppTheme(): AppTheme = AppTheme.byNumber(
        settingsHelper.getInt(SettingKeys.APP_THEME_KEY, DEFAULT_APP_THEME)
    )

    override fun setAppTheme(appTheme: AppTheme) {
        settingsHelper.putInt(SettingKeys.APP_THEME_KEY, appTheme.orderNumber)
    }

    override fun getAppUiMode(): AppUiMode = AppUiMode.byNumber(
        settingsHelper.getInt(SettingKeys.APP_MODE_KEY, DEFAULT_APP_MODE)
    )

    override fun setAppUiMode(appUiMode: AppUiMode) {
        settingsHelper.putInt(SettingKeys.APP_MODE_KEY, appUiMode.orderNumber)
    }

    override fun getSettingsChangedLiveData(): LiveData<SettingsAction> {
        return settingsChangedLiveData
    }

    private inner class SettingsChangedLiveData : LiveData<SettingsAction>() {
        override fun onActive() {
            super.onActive()

            settingsHelper.addPreferenceChangeListener { key ->
                when (key) {
                    SettingKeys.APP_THEME_KEY -> {
                        value = SettingsAction.AppThemeChanged(getAppTheme())
                    }
                    SettingKeys.APP_MODE_KEY -> {
                        value = SettingsAction.AppUiModeChanged(getAppUiMode())
                    }
                }
            }
        }

        override fun onInactive() {
            super.onInactive()

            settingsHelper.removePreferenceChangeListener()
        }
    }
}
