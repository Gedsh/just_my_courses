package pan.alexander.logintask.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

const val DISPATCHER_MAIN = "DISPATCHER_MAIN"
const val DISPATCHER_IO = "DISPATCHER_IO"

@Module
class CoroutinesModule {

    @Provides
    @Named(DISPATCHER_MAIN)
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Named(DISPATCHER_IO)
    fun provideDispatcherIo(): CoroutineDispatcher = Dispatchers.IO
}
