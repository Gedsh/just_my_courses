package pan.alexander.fileconverter.di

import dagger.Component
import pan.alexander.fileconverter.ui.MainActivity
import javax.inject.Singleton

@Singleton
@Component(
    modules = [PresentersModule::class, UtilsModule::class, ContextModule::class,
        DataSourceModule::class, RepositoryModule::class, InteractorsModule::class,
        EventBusModule::class]
)
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)
}
