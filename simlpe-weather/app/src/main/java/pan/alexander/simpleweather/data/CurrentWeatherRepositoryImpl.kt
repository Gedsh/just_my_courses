package pan.alexander.simpleweather.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import pan.alexander.simpleweather.App
import pan.alexander.simpleweather.UpdateDBWorker
import pan.alexander.simpleweather.domain.CurrentWeatherRepository
import pan.alexander.simpleweather.domain.entities.CurrentWeather
import javax.inject.Inject

class CurrentWeatherRepositoryImpl @Inject constructor(): CurrentWeatherRepository {

    private val dao = App.instance.daggerComponent.getCurrentWeatherDao()

    override fun getAllWeatherFromDB(): List<CurrentWeather> {
       return dao.getAllSavedWeather()
    }

    override fun getCurrentWeatherFromDB(): CurrentWeather {
        return dao.getLatestSavedWeather()
    }

    override fun updateCurrentWeatherInDB(weather: CurrentWeather) {
        dao.update(weather)
    }

    override fun addCurrentWeatherToDB(weather: CurrentWeather) {
        dao.insert(weather)
    }

    override fun deleteWeatherFromDB(weather: CurrentWeather) {
        dao.delete(weather)
    }

    override fun deleteAllWeatherFromDB() {
        dao.deleteAllRows()
    }

    override fun loadCurrentWeatherData(context: Context) {
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val loadCurrentWeatherRequest = OneTimeWorkRequest.Builder(UpdateDBWorker::class.java)
            .setConstraints(constraints)
            .addTag("loadCurrentWeather")
            .build()
        WorkManager.getInstance(context).enqueue(loadCurrentWeatherRequest)
    }
}
