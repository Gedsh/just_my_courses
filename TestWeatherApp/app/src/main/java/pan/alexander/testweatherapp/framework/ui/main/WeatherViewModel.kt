package pan.alexander.testweatherapp.framework.ui.main

import androidx.lifecycle.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import pan.alexander.testweatherapp.domain.CurrentWeatherInteractor
import pan.alexander.testweatherapp.domain.entities.CurrentWeather
import pan.alexander.testweatherapp.utils.AppConstants.INTERVAL_REFRESH_WEATHER
import java.util.*

class WeatherViewModel(
    private val interactor: CurrentWeatherInteractor
) : ViewModel() {

    private val currentWeatherActionData = MutableLiveData<CurrentWeatherActionData>()

    private var loadingCurrentWeatherJob: Job? = null
    private var loadingSavedWeatherJob: Job? = null

    fun getCurrentWeatherActionData(): LiveData<CurrentWeatherActionData> {
        val job = loadingSavedWeatherJob
        if (job == null || job.isCompleted) {
            loadingSavedWeatherJob = loadSavedWeatherFromDb()
        }
        return currentWeatherActionData.distinctUntilChanged()
    }

    fun requestCurrentWeather(zipCode: Int) {
        currentWeatherActionData.value = CurrentWeatherActionData.Loading
        loadingCurrentWeatherJob?.cancel()
        loadingCurrentWeatherJob = loadCurrentWeatherFromServer(zipCode)
    }

    private fun loadSavedWeatherFromDb() = viewModelScope.launch {
        interactor.getCurrentWeatherFromDb()
            .filter { it.isNotEmpty() }
            .catch { currentWeatherActionData.value = CurrentWeatherActionData.Error(it) }
            .collect {
                val savedWeather = it.first()
                currentWeatherActionData.value = CurrentWeatherActionData.Success(savedWeather)

                refreshWeatherIfRequired(savedWeather)
            }
    }

    private suspend fun refreshWeatherIfRequired(savedWeather: CurrentWeather) {
        if (Date().time - savedWeather.timestamp.time > INTERVAL_REFRESH_WEATHER) {
            requestCurrentWeather(savedWeather.zipCode)
        } else {
            if (loadingCurrentWeatherJob == null) {
                delay(INTERVAL_REFRESH_WEATHER)
            }
            if (loadingCurrentWeatherJob == null) {
                requestCurrentWeather(savedWeather.zipCode)
            }
        }
    }

    private fun loadCurrentWeatherFromServer(zipCode: Int) = viewModelScope.launch {
        kotlin.runCatching {
            interactor.requestCurrentWeather(zipCode)
        }.onFailure {
            if (it !is CancellationException) {
                currentWeatherActionData.value = CurrentWeatherActionData.Error(it)
            }
        }
    }
}
