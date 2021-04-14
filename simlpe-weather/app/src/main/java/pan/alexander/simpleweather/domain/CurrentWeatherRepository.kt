package pan.alexander.simpleweather.domain

import android.content.Context
import androidx.lifecycle.LiveData
import pan.alexander.simpleweather.domain.entities.CurrentWeather

interface CurrentWeatherRepository {
    fun getAllWeatherFromDB(): LiveData<List<CurrentWeather>>
    fun getCurrentWeatherFromDB(): LiveData<List<CurrentWeather>>
    fun updateCurrentWeatherInDB(weather: CurrentWeather)
    fun addCurrentWeatherToDB(weather: CurrentWeather)
    fun deleteWeatherFromDB(weather: CurrentWeather)
    fun deleteAllWeatherFromDB()

    fun loadCurrentWeatherData(context: Context)
}
