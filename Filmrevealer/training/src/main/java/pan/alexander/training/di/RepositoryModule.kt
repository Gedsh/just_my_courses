package pan.alexander.training.di

import dagger.Module
import dagger.Provides
import pan.alexander.training.data.ContactsRepositoryImplementation
import pan.alexander.training.domain.ContactsRepository

@Module
class RepositoryModule {

    @Provides
    fun provideContactsRepository(): ContactsRepository = ContactsRepositoryImplementation()
}
