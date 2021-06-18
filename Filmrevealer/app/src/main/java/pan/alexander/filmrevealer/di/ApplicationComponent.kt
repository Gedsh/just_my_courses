package pan.alexander.filmrevealer.di

import dagger.Component
import pan.alexander.filmrevealer.data.database.FilmDao
import pan.alexander.filmrevealer.data.web.FilmsApiService
import pan.alexander.filmrevealer.domain.MainInteractor
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class, RoomModule::class, RepositoryModule::class])
interface ApplicationComponent {
    fun getFilmsApiService(): dagger.Lazy<FilmsApiService>
    fun getFilmDao(): dagger.Lazy<FilmDao>
    fun getMainInteractor(): dagger.Lazy<MainInteractor>
}
