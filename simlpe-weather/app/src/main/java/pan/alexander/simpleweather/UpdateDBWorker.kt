package pan.alexander.simpleweather

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import pan.alexander.simpleweather.App.Companion.LOG_TAG
import pan.alexander.simpleweather.domain.entities.CurrentWeather
import java.io.IOException

class UpdateDBWorker(
    private val context: Context,
    workerParameters: WorkerParameters
): Worker(context, workerParameters) {
    private val api = App.instance.daggerComponent.getCurrentWeatherApiService()
    private val dao = App.instance.daggerComponent.getCurrentWeatherDao()

    override fun doWork(): Result {
        try {
            return doApiRequest()
        } catch (e: IOException) {
            Log.e(LOG_TAG, "UpdateDBWorker exception ${e.message} ${e.cause}")
        }

        return Result.failure()
    }

    private fun doApiRequest(): Result {
        val key = Base64.decode(context.getString(R.string.api_key), Base64.DEFAULT).toString(charset("UTF-8"))

        val response = api.getWeatherForCity("London", key).execute()
        if (response.isSuccessful) {
            response.body()?.let {
                val currentWeather = CurrentWeather(it)

                Log.i(LOG_TAG, "UpdateDBWorker save data $currentWeather")

                dao.insert(currentWeather)
            }

            Log.i(LOG_TAG, "UpdateDBWorker successfully get data ${response.headers()} ${response.body()}")

            return Result.success()
        }

        Log.w(LOG_TAG, "UpdateDBWorker cannot get data ${response.headers()} ${response.body()}")

        return Result.retry()
    }

}
