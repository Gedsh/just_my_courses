package pan.alexander.training.di

import dagger.Component
import pan.alexander.training.domain.MainInteractor

@Component(
    modules = [ContentResolverModule::class, RepositoryModule::class,
        CoroutinesModule::class, LocationModule::class, ContextModule::class]
)
interface ApplicationComponent {
    fun getMainInteractor(): dagger.Lazy<MainInteractor>
}
