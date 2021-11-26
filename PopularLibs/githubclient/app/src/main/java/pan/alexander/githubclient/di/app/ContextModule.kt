package pan.alexander.githubclient.di.app

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ContextModule(
    private val context: Context
) {
    @Provides
    fun provideAppContext() = context
}
