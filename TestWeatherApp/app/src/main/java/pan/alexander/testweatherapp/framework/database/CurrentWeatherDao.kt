package pan.alexander.testweatherapp.framework.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pan.alexander.testweatherapp.domain.entities.CurrentWeather

@Dao
interface CurrentWeatherDao {
    @Query("SELECT * FROM CurrentWeather ORDER BY timestamp DESC LIMIT 1")
    fun getCurrentWeather(): Flow<List<CurrentWeather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentWeather(weather: CurrentWeather)
}
