package pan.alexander.pictureoftheday.domain.settings

sealed class SettingsAction {
    data class AppThemeChanged(val appTheme: AppTheme) : SettingsAction()
    data class AppUiModeChanged(val appUiMode: AppUiMode) : SettingsAction()
}
