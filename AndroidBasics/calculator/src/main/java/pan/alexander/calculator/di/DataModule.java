package pan.alexander.calculator.di;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pan.alexander.calculator.data.RepositoryDao;

@Module
public class DataModule {
    private final RepositoryDao repositoryDao;

    public DataModule(SharedPreferences sharedPreferences) {
        repositoryDao = new RepositoryDao(sharedPreferences);
    }

    @Provides
    @Singleton
    public RepositoryDao providesRepositoryDao() {
        return repositoryDao;
    }
}
