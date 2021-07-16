package pan.alexander.training.di

import android.content.ContentResolver
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ContentResolverModule(private val context: Context) {

    @Provides
    fun provideContentResolver(): ContentResolver = context.contentResolver
}
