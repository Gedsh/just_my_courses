package pan.alexander.calculator.di;;

import javax.inject.Singleton;

import dagger.Component;
import pan.alexander.calculator.data.RepositoryDao;
import pan.alexander.calculator.domain.Calculator;
import pan.alexander.calculator.domain.MainInteractor;

@Singleton
@Component(modules = {DataModule.class, RepositoryModule.class})
public interface ApplicationComponent {
    RepositoryDao getRepositoryDao();
    MainInteractor getMainInteractor();
    Calculator getCalculator();
}

