package pan.alexander.simpleweather.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import pan.alexander.simpleweather.domain.entities.CurrentWeather

@Database(version = 1, entities = [CurrentWeather::class])
@TypeConverters(MyConverters::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun currentWeatherDao(): CurrentWeatherDao
}

class MyConverters {
    @TypeConverter
    fun listToJson(value: List<String>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<String> {
        val objects = Gson().fromJson(value, Array<String>::class.java) as Array<String>
        return objects.toList()
    }

}