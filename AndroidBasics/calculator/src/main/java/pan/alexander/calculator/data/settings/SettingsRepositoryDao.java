package pan.alexander.calculator.data.settings;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class SettingsRepositoryDao {
    private final SharedPreferences sharedPreferences;

    public SettingsRepositoryDao(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    public String getStringFromDefaultSharedPreference(@NonNull String key) {
        return sharedPreferences.getString(key, "");
    }

    public void saveStringToDefaultSharedPreference(@NonNull String key, @NonNull String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }
}
