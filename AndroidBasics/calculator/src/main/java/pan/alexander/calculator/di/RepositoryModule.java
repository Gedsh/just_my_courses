package pan.alexander.calculator.di;

import dagger.Module;
import dagger.Provides;
import pan.alexander.calculator.data.DataRepositoryImplementation;
import pan.alexander.calculator.domain.DataRepository;

@Module
public class RepositoryModule {
    @Provides
    public DataRepository providesDataRepository() {
        return new DataRepositoryImplementation();
    }
}
