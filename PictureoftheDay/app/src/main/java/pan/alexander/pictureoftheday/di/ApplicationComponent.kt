package pan.alexander.pictureoftheday.di

import dagger.Component
import pan.alexander.pictureoftheday.domain.picture.PictureInteractor
import pan.alexander.pictureoftheday.domain.settings.SettingsRepository
import javax.inject.Singleton

@Singleton
@Component(
    modules = [RetrofitModule::class, ConfigurationModule::class, CoroutinesModule::class,
        DataSourcesModule::class, RepositoryModule::class, InteractorsModule::class,
        PreferencesModule::class]
)
interface ApplicationComponent {
    fun getMainInteractor(): dagger.Lazy<PictureInteractor>
    fun getSettingsRepository(): dagger.Lazy<SettingsRepository>
}
