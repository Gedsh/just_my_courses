package pan.alexander.pictureoftheday.data.mars

import kotlinx.coroutines.withContext
import pan.alexander.pictureoftheday.data.mars.model.MarsServerResponse
import pan.alexander.pictureoftheday.framework.web.nasaimage.NasaApiService
import pan.alexander.pictureoftheday.utils.configuration.ConfigurationManager
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

private const val CURIOSITY_NAME = "curiosity"

class MarsPhotosDataSourceImpl @Inject constructor(
    private val nasaApiService: NasaApiService,
    private val configurationManager: ConfigurationManager,
    private val dispatcherIO: CoroutineContext
) : MarsPhotosDataSource {
    override suspend fun getCuriosityPhotosBySol(sol: Int): Response<MarsServerResponse> {
        return withContext(dispatcherIO) {
            nasaApiService.getMarsPhotosByRoverAndSol(
                CURIOSITY_NAME,
                sol,
                configurationManager.getApiKey()
            )
        }
    }
}
