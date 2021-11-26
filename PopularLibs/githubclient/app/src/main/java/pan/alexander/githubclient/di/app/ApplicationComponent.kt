package pan.alexander.githubclient.di.app

import dagger.Component
import pan.alexander.githubclient.di.main.MainComponent
import javax.inject.Singleton

@Singleton
@Component(
    modules = [RepositoryModule::class, CiceroneModule::class, NavigationModule::class,
        EventBusModule::class, ManagersModule::class, RetrofitModule::class,
        DataSourceModule::class, ImageLoaderModule::class, RoomModule::class,
        ContextModule::class, AppSubcomponentsModule::class]
)
interface ApplicationComponent {
    fun mainComponent(): MainComponent.Factory
}
