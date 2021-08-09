package pan.alexander.pictureoftheday.data.epic

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import pan.alexander.pictureoftheday.domain.epic.EpicRepository
import pan.alexander.pictureoftheday.domain.epic.entities.EpicImage
import java.lang.RuntimeException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class EpicRepositoryImpl @Inject constructor(
    private val epicDataSource: EpicDataSource,
    private val serverResponseToEpicImageMapper: ServerResponseToEpicImageMapper,
    private val dispatcherIO: CoroutineContext
) : EpicRepository {
    override suspend fun getRecentEnhancedColorImage(): Flow<EpicImage> {
        return flow {
            epicDataSource.getRecentEnhancedColorImage().let {
                if (it.isSuccessful && it.body() != null) {
                    it.body()?.takeIf { body -> body.isNotEmpty() }?.let { serverResponse ->
                        emit(serverResponseToEpicImageMapper.map(serverResponse.random()))
                    }
                } else {
                    it.errorBody()?.let { responseBody ->
                        loadErrorBody(responseBody)
                    }
                    throw RuntimeException("Unidentified error")
                }
            }
        }
    }

    private suspend fun loadErrorBody(responseBody: ResponseBody) = withContext(dispatcherIO) {
        kotlin.runCatching {
            responseBody.string()
        }.onSuccess { error ->
            if (error.isNotBlank()) {
                throw RuntimeException(error)
            }
        }.onFailure {
            throw RuntimeException(it.message)
        }
    }
}
