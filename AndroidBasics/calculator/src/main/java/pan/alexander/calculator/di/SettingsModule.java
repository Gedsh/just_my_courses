package pan.alexander.calculator.di;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pan.alexander.calculator.data.settings.SettingsRepositoryDao;

@Module
public class SettingsModule {
    private final SettingsRepositoryDao settingsRepositoryDao;

    public SettingsModule(SharedPreferences sharedPreferences) {
        settingsRepositoryDao = new SettingsRepositoryDao(sharedPreferences);
    }

    @Provides
    @Singleton
    public SettingsRepositoryDao providesSettingsRepositoryDao() {
        return settingsRepositoryDao;
    }
}
