package pan.alexander.training.di

import dagger.Component
import pan.alexander.training.data.ContactsDao
import pan.alexander.training.data.ContactsLiveData
import pan.alexander.training.domain.ContactsRepository
import pan.alexander.training.domain.MainInteractor

@Component(
    modules = [ContentResolverModule::class, RepositoryModule::class, CoroutinesModule::class]
)
interface ApplicationComponent {
    fun getContactsRepository(): dagger.Lazy<ContactsRepository>
    fun getContactsDao(): dagger.Lazy<ContactsDao>
    fun getContactsLiveData(): dagger.Lazy<ContactsLiveData>
    fun getMainInteractor(): dagger.Lazy<MainInteractor>
}
