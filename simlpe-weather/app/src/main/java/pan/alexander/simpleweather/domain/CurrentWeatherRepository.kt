package pan.alexander.simpleweather.domain

import pan.alexander.simpleweather.domain.entities.CurrentWeather

interface CurrentWeatherRepository {
    fun getAllWeatherFromDB(): List<CurrentWeather>
    fun getCurrentWeatherFromDB(): CurrentWeather
    fun updateCurrentWeatherInDB(weather: CurrentWeather)
    fun addCurrentWeatherToDB(weather: CurrentWeather)
    fun deleteWeatherFromDB(weather: CurrentWeather)
    fun deleteAllWeatherFromDB()

    fun loadCurrentWeatherData()
}