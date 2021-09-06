package pan.alexander.testweatherapp.domain.entities

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Keep
@Entity
data class CurrentWeather(
    @PrimaryKey
    @ColumnInfo(name = "zip_code") val zipCode: Int = 0,
    val location: String,
    val temperature: Float,
    @ColumnInfo(name = "wind_speed") val windSpeed: Float,
    val humidity: Int,
    val visibility: String,
    @ColumnInfo(name = "sunrise_time") val sunriseTime: Date,
    @ColumnInfo(name = "sunset_time") val sunsetTime: Date,
    val timestamp: Date = Date()
)
