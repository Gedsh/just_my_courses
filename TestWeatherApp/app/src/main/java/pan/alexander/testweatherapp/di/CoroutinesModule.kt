package pan.alexander.testweatherapp.di

import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

object CoroutinesModule {
    fun provideDispatcherIO() = module {
        factory<CoroutineContext> { Dispatchers.IO }
    }
}
