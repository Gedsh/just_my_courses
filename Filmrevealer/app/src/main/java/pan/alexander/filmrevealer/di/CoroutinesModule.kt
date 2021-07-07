package pan.alexander.filmrevealer.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class CoroutinesModule {

    @Provides
    fun provideDispatcherIO(): CoroutineDispatcher = Dispatchers.IO
}
