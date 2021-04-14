package pan.alexander.simpleweather.di

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import pan.alexander.simpleweather.data.database.AppDatabase
import pan.alexander.simpleweather.data.database.CurrentWeatherDao
import javax.inject.Singleton

@Module
class RoomModule(appContext: Application) {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE CurrentWeather ADD COLUMN rain INTEGER DEFAULT -1 NOT NULL")
            database.execSQL("ALTER TABLE CurrentWeather ADD COLUMN snow INTEGER DEFAULT -1 NOT NULL")
        }
    }

    private val appDatabase = Room
        .databaseBuilder(appContext, AppDatabase::class.java, "main_database")
        .addMigrations(MIGRATION_1_2)
        .build()

    @Provides
    @Singleton
    fun providesRoomDatabase(): AppDatabase = appDatabase

    @Provides
    @Singleton
    fun providesCurrentWeatherDao(): CurrentWeatherDao = appDatabase.currentWeatherDao()
}
