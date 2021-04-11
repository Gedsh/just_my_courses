package pan.alexander.simpleweather.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import pan.alexander.simpleweather.data.database.AppDatabase
import pan.alexander.simpleweather.data.database.CurrentWeatherDao
import javax.inject.Singleton

@Module
class RoomModule(appContext: Application) {
    private val appDatabase = Room
        .databaseBuilder(appContext, AppDatabase::class.java, "main_database")
        .fallbackToDestructiveMigration()
        .allowMainThreadQueries()
        .build()

    @Provides
    @Singleton
    fun providesRoomDatabase(): AppDatabase = appDatabase

    @Provides
    @Singleton
    fun providesCurrentWeatherDao(): CurrentWeatherDao = appDatabase.currentWeatherDao()
}
