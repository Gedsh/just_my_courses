package pan.alexander.filmrevealer.di

import android.os.Handler
import dagger.Component
import pan.alexander.filmrevealer.data.database.FilmDao
import pan.alexander.filmrevealer.data.database.FilmDetailsDao
import pan.alexander.filmrevealer.data.web.FilmsApiService
import pan.alexander.filmrevealer.domain.MainInteractor
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class, RoomModule::class, RepositoryModule::class, MainThreadHandler::class])
interface ApplicationComponent {
    fun getFilmsApiService(): dagger.Lazy<FilmsApiService>
    fun getFilmDao(): dagger.Lazy<FilmDao>
    fun getFilmDetailsDao(): dagger.Lazy<FilmDetailsDao>
    fun getMainInteractor(): dagger.Lazy<MainInteractor>
    fun getMainHandler(): dagger.Lazy<Handler>
}
