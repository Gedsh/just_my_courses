package pan.alexander.training.di

import dagger.Binds
import dagger.Module
import pan.alexander.training.data.ContactsRepositoryImplementation
import pan.alexander.training.data.LocationRepositoryImplementation
import pan.alexander.training.domain.ContactsRepository
import pan.alexander.training.domain.LocationRepository

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun provideContactsRepository(
        contactsRepositoryImplementation: ContactsRepositoryImplementation
    ): ContactsRepository

    @Binds
    abstract fun provideLocationRepository(
        locationRepositoryImplementation: LocationRepositoryImplementation
    ): LocationRepository
}
