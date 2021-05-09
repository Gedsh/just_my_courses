package pan.alexander.calculator.di;

import dagger.Module;
import dagger.Provides;
import pan.alexander.calculator.data.HistoryRepositoryImplementation;
import pan.alexander.calculator.data.SettingsRepositoryImplementation;
import pan.alexander.calculator.domain.HistoryRepository;
import pan.alexander.calculator.domain.SettingsRepository;

@Module
public class RepositoryModule {
    @Provides
    public HistoryRepository providesHistoryRepository() {
        return new HistoryRepositoryImplementation();
    }

    @Provides
    public SettingsRepository providesSettingsRepository() {
        return new SettingsRepositoryImplementation();
    }
}
