package pan.alexander.simpleweather.data.database

import androidx.room.*
import pan.alexander.simpleweather.domain.entities.CurrentWeather

@Dao
interface CurrentWeatherDao {

    @Query("SELECT * FROM CurrentWeather")
    fun getAllSavedWeather(): List<CurrentWeather>

    @Query("SELECT * FROM CurrentWeather ORDER BY ID DESC LIMIT 1")
    fun getLatestSavedWeather(): CurrentWeather

    @Query("DELETE FROM CurrentWeather")
    fun deleteAllRows()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weather: CurrentWeather)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(weather: CurrentWeather)

    @Delete
    fun delete(weather: CurrentWeather)
}