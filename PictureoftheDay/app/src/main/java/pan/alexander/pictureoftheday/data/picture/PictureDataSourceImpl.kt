package pan.alexander.pictureoftheday.data.picture

import kotlinx.coroutines.withContext
import pan.alexander.pictureoftheday.data.picture.model.ApiServerResponse
import pan.alexander.pictureoftheday.framework.web.nasapicture.NasaPodApiService
import pan.alexander.pictureoftheday.utils.configuration.ConfigurationManager
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PictureDataSourceImpl @Inject constructor(
    private val nasaPodApiService: NasaPodApiService,
    private val configurationManager: ConfigurationManager,
    private val dispatcherIO: CoroutineContext
) : PictureDataSource {
    override suspend fun getPhotoByDate(dateStr: String): Response<ApiServerResponse> {
        return withContext(dispatcherIO) {
            nasaPodApiService.getPictureByDateStr(configurationManager.getApiKey(), dateStr)
        }
    }
}
