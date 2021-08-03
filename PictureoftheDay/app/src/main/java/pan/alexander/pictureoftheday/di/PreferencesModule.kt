package pan.alexander.pictureoftheday.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

import dagger.Module
import dagger.Provides

private const val SETTINGS_SHARED_PREFERENCES_NAME = "app_settings"

@Module
class PreferencesModule(val context: Context) {
    @Provides
    fun provideSettingsSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(SETTINGS_SHARED_PREFERENCES_NAME, MODE_PRIVATE)
    }
}
