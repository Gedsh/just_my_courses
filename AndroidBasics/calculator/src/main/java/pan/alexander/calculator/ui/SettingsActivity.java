package pan.alexander.calculator.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import pan.alexander.calculator.App;
import pan.alexander.calculator.R;
import pan.alexander.calculator.domain.MainInteractor;
import pan.alexander.calculator.util.Utils;

import static pan.alexander.calculator.util.Preference.VIEW_MODE_PREFERENCE;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        setupActionBar();

        App.getInstance().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.settingsActionBarTitle);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(VIEW_MODE_PREFERENCE)) {
            setViewMode();
            recreate();
        }
    }

    private void setViewMode() {

        MainInteractor mainInteractor = App.getInstance().getDaggerComponent().getMainInteractor();

        String viewModeValue = mainInteractor.getStringPreference(VIEW_MODE_PREFERENCE);

        Utils.setViewMode(viewModeValue);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.fragment_settings, rootKey);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else {
            return super.onOptionsItemSelected(item);
        }

        return false;
    }

    @Override
    protected void onDestroy() {

        App.getInstance().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

        super.onDestroy();
    }
}
