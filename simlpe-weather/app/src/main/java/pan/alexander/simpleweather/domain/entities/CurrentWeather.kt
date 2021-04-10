package pan.alexander.simpleweather.domain.entities
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import pan.alexander.simpleweather.data.web.pojo.CurrentWeatherData

@Entity
data class CurrentWeather(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var country: String = "",
    var city: String = "",
    @ColumnInfo(name = "current_condition") var currentCondition: String = "",
    @ColumnInfo(name = "condition_description") var conditionDescription: String = "",
    @ColumnInfo(name = "data_source") var dataSource: String = "",
    var temperature: Float = .0f,
    @ColumnInfo(name = "feels_like") var feelsLike: Float = .0f,
    var pressure: Int = 0,
    var humidity: Int = 0,
    @ColumnInfo(name = "wind_speed") var windSpeed: Float = .0f,
    @ColumnInfo(name = "wind_direction") var windDirection: Int = 0,
    var cloudiness: Int = 0,
    @ColumnInfo(name = "icon_code") var iconCode: String = "",
    @ColumnInfo(name = "entry_time") var entryTime: Long = System.currentTimeMillis()
) {
    constructor(currentWeatherData: CurrentWeatherData): this() {
        this.country = currentWeatherData.systemData.country
        this.city = currentWeatherData.name
        this.currentCondition = currentWeatherData.weather.get(0).condition
        this.conditionDescription = currentWeatherData.weather.get(0).description
        this.temperature = currentWeatherData.main.temperature
        this.feelsLike = currentWeatherData.main.feelsLike
        this.pressure = currentWeatherData.main.pressure
        this.humidity = currentWeatherData.main.humidity
        this.windSpeed = currentWeatherData.wind.speed
        this.windDirection = currentWeatherData.wind.deg
        this.cloudiness = currentWeatherData.clouds.cloudiness
        this.iconCode = currentWeatherData.weather.get(0).iconId
    }
}