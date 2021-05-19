package pan.alexander.calculator.data;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import pan.alexander.calculator.App;
import pan.alexander.calculator.data.settings.SettingsRepositoryDao;
import pan.alexander.calculator.domain.SettingsRepository;

public class SettingsRepositoryImplementation implements SettingsRepository {
    private final SettingsRepositoryDao dao = App.getInstance().getDaggerComponent().getSettingsRepositoryDao();

    @Inject
    public SettingsRepositoryImplementation() {
    }

    @Override
    public String getSettings(@NonNull String key) {
        return dao.getStringFromDefaultSharedPreference(key);
    }

    @Override
    public void saveSettings(@NonNull String key, @NonNull String value) {
        dao.saveStringToDefaultSharedPreference(key, value);
    }
}
