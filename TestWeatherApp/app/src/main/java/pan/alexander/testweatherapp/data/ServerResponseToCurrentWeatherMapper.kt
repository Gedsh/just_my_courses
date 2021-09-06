package pan.alexander.testweatherapp.data

import pan.alexander.testweatherapp.data.model.CurrentWeatherServerResponse
import pan.alexander.testweatherapp.domain.entities.CurrentWeather
import java.util.*

class ServerResponseToCurrentWeatherMapper {
    fun map(zipCode: Int, serverResponse: CurrentWeatherServerResponse): CurrentWeather {
        return CurrentWeather(
            zipCode = zipCode,
            location = serverResponse.name,
            temperature = serverResponse.main.temperature,
            windSpeed = serverResponse.wind.speed,
            humidity = serverResponse.main.humidity,
            visibility = serverResponse.weather.firstOrNull()?.condition ?: "",
            sunriseTime = Date(
                (serverResponse.systemData.sunrise - serverResponse.timeZoneShift) * 1000
            ),
            sunsetTime = Date(
                (serverResponse.systemData.sunset - serverResponse.timeZoneShift) * 1000
            )
        )
    }
}
