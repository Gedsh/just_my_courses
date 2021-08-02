package pan.alexander.pictureoftheday.di

import dagger.Component
import pan.alexander.pictureoftheday.domain.picture.PictureInteractor
import javax.inject.Singleton

@Singleton
@Component(
    modules = [RetrofitModule::class, ConfigurationModule::class, CoroutinesModule::class,
        DataSourcesModule::class, RepositoryModule::class, InteractorsModule::class]
)
interface ApplicationComponent {
    fun getMainInteractor(): dagger.Lazy<PictureInteractor>
}
