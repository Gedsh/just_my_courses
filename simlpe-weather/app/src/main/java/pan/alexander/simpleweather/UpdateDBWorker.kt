package pan.alexander.simpleweather

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class UpdateDBWorker(
    private val context: Context,
    workerParameters: WorkerParameters
): Worker(context, workerParameters) {
    private val api = App.instance.daggerComponent.getCurrentWeatherApiService()
    private val dao = App.instance.daggerComponent.getCurrentWeatherDao()

    override fun doWork(): Result {
        TODO("Not yet implemented")
    }

}