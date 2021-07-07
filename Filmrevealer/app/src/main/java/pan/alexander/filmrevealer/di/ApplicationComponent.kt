package pan.alexander.filmrevealer.di

import android.os.Handler
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Component
import pan.alexander.filmrevealer.data.database.FilmDao
import pan.alexander.filmrevealer.data.database.FilmDetailsDao
import pan.alexander.filmrevealer.data.web.FilmsApiService
import pan.alexander.filmrevealer.domain.MainInteractor
import javax.inject.Singleton

@Singleton
@Component(
    modules = [RetrofitModule::class, RoomModule::class, RepositoryModule::class,
        MainThreadHandler::class, DataStoreModule::class, CoroutinesModule::class]
)
interface ApplicationComponent {
    fun getFilmsApiService(): dagger.Lazy<FilmsApiService>
    fun getFilmDao(): dagger.Lazy<FilmDao>
    fun getFilmDetailsDao(): dagger.Lazy<FilmDetailsDao>
    fun getMainInteractor(): dagger.Lazy<MainInteractor>
    fun getMainHandler(): dagger.Lazy<Handler>
    fun getDataStore(): dagger.Lazy<DataStore<Preferences>>
}
