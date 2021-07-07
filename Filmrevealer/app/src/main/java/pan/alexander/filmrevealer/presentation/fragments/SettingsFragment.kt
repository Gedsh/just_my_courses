package pan.alexander.filmrevealer.presentation.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import pan.alexander.filmrevealer.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}
