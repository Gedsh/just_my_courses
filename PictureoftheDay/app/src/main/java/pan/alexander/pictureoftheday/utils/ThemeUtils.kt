package pan.alexander.pictureoftheday.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import pan.alexander.pictureoftheday.R
import pan.alexander.pictureoftheday.domain.settings.AppTheme
import pan.alexander.pictureoftheday.domain.settings.AppUiMode

object ThemeUtils {
    fun setAppTheme(context: Context, appTheme: AppTheme) {
        when (appTheme) {
            AppTheme.PURPLE -> context.setTheme(R.style.Theme_PictureOfTheDay_Purple)
            AppTheme.PINK -> context.setTheme(R.style.Theme_PictureOfTheDay_Pink)
            AppTheme.INDIGO -> context.setTheme(R.style.Theme_PictureOfTheDay_Indigo)
        }
    }

    fun setAppUiMode(appUiMode: AppUiMode) {
        when (appUiMode) {
            AppUiMode.MODE_AUTO -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            AppUiMode.MODE_DAY -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            AppUiMode.MODE_NIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}
