package pan.alexander.githubclient.di

import dagger.Binds
import dagger.Module
import pan.alexander.githubclient.utils.eventbus.Error
import pan.alexander.githubclient.utils.eventbus.ErrorEventBus
import pan.alexander.githubclient.utils.eventbus.EventBus

@Module
abstract class EventBusModule {

    @Binds
    abstract fun provideErrorEventBus(errorEventBus: ErrorEventBus): EventBus<Error>
}
