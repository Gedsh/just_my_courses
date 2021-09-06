package pan.alexander.filmrevealer.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

private const val USER_PREFERENCES_NAME = "user_preferences"

@Module
class DataStoreModule(val context: Context) {

    @Provides
    @Singleton
    fun provideDataStore() = PreferenceDataStoreFactory.create {
        context.preferencesDataStoreFile(USER_PREFERENCES_NAME)
    }

    @Provides
    @Singleton
    fun provideRxDataStore() = RxPreferenceDataStoreBuilder(context, USER_PREFERENCES_NAME).build()
}
