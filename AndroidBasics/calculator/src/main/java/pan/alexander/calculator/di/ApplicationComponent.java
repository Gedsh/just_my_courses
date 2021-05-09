package pan.alexander.calculator.di;

import javax.inject.Singleton;

import dagger.Component;
import pan.alexander.calculator.data.settings.SettingsRepositoryDao;
import pan.alexander.calculator.data.database.HistoryDao;
import pan.alexander.calculator.domain.Calculator;
import pan.alexander.calculator.domain.MainInteractor;

@Singleton
@Component(modules = {SettingsModule.class, RepositoryModule.class, RoomModule.class})
public interface ApplicationComponent {
    HistoryDao getHistoryDao();
    SettingsRepositoryDao getSettingsRepositoryDao();
    MainInteractor getMainInteractor();
    Calculator getCalculator();
}
