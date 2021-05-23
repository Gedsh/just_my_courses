package pan.alexander.notes.presentation.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import pan.alexander.notes.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}
