package pan.alexander.filmrevealer.di

import android.content.Context
import android.os.Handler
import dagger.Module
import dagger.Provides

@Module
class MainThreadHandler(val context: Context) {

    @Provides
    fun provideHandler() = Handler(context.mainLooper)
}
