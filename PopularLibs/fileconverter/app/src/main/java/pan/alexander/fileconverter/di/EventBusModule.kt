package pan.alexander.fileconverter.di

import dagger.Binds
import dagger.Module
import pan.alexander.fileconverter.utils.eventbus.AppError
import pan.alexander.fileconverter.utils.eventbus.ErrorEventBus
import pan.alexander.fileconverter.utils.eventbus.EventBus

@Module
abstract class EventBusModule {

    @Binds
    abstract fun provideErrorEventBus(errorEventBus: ErrorEventBus): EventBus<AppError>
}
