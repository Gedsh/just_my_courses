package pan.alexander.testweatherapp.di

import android.content.Context
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pan.alexander.testweatherapp.framework.database.AppDatabase

private const val APP_DATABASE_NAME = "main_database"

object RoomModule {

    fun provideAppDatabase() = module {
        single { provideDatabase(androidContext()) }
        single { provideCurrentWeatherDao(appDatabase = get()) }
    }

    private fun provideDatabase(context: Context) = Room
        .databaseBuilder(context, AppDatabase::class.java, APP_DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()

    private fun provideCurrentWeatherDao(appDatabase: AppDatabase) = appDatabase.currentWeatherDao()
}
