package pan.alexander.testweatherapp.framework.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pan.alexander.testweatherapp.domain.entities.CurrentWeather

@Database(entities = [CurrentWeather::class], version = 1)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
}
