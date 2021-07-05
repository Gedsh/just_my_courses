package pan.alexander.notes.di;

import dagger.Module;
import dagger.Provides;
import pan.alexander.notes.data.AccountRepositoryImplementation;
import pan.alexander.notes.domain.AccountRepository;

@Module
public class AccountModule {

    @Provides
    AccountRepository providesAccountRepository() {
        return new AccountRepositoryImplementation();
    }
}
