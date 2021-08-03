package pan.alexander.pictureoftheday.framework.preferences

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsHelper @Inject constructor(
    private val settingsSharedPreferences: SharedPreferences
) {

    private var onPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener? =
        null

    fun getInt(key: String, defaultValue: Int): Int {
        return settingsSharedPreferences.getInt(key, defaultValue)
    }

    fun putInt(key: String, value: Int) {
        settingsSharedPreferences
            .edit()
            .putInt(key, value)
            .apply()
    }

    fun addPreferenceChangeListener(block: (key: String) -> Unit) {

        onPreferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            block(key)
        }

        settingsSharedPreferences.registerOnSharedPreferenceChangeListener(
            onPreferenceChangeListener
        )
    }

    fun removePreferenceChangeListener() {
        settingsSharedPreferences.unregisterOnSharedPreferenceChangeListener(
            onPreferenceChangeListener
        )
        onPreferenceChangeListener = null
    }
}
