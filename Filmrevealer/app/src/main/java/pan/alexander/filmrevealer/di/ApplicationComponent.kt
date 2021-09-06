package pan.alexander.filmrevealer.di

import dagger.Component
import pan.alexander.filmrevealer.domain.interactors.MainInteractor
import pan.alexander.filmrevealer.services.RateFilmIntentService
import javax.inject.Singleton

@Singleton
@Component(
    modules = [RetrofitModule::class, RoomModule::class,
        DataStoreModule::class, DataSourcesModule::class,
        RepositoryModule::class, ManagersModule::class,
        InteractorsModule::class]
)
interface ApplicationComponent {
    fun getMainInteractor(): dagger.Lazy<MainInteractor>
    fun inject(service: RateFilmIntentService)
}
