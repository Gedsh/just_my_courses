package pan.alexander.pictureoftheday.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Module
class CoroutinesModule {
    @Provides
    fun provideDispatcherIO(): CoroutineContext = Dispatchers.IO
}
