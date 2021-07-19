package pan.alexander.training.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope

@Module
class CoroutinesModule {

    @Provides
    fun provideDispatcherIO(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideMainScope(): CoroutineScope = MainScope()
}
