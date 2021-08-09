package pan.alexander.pictureoftheday.data.mars

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import pan.alexander.pictureoftheday.domain.mars.MarsRepository
import pan.alexander.pictureoftheday.domain.mars.entities.MarsPhoto
import java.lang.RuntimeException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MarsRepositoryImpl @Inject constructor(
    private val marsPhotosDataSource: MarsPhotosDataSource,
    private val marsPhotoApiToMarsPhotoMapper: MarsPhotoApiToMarsPhotoMapper,
    private val dispatcherIO: CoroutineContext
) : MarsRepository {
    override fun getCuriosityPhotoBySol(sol: Int): Flow<MarsPhoto> {
        return flow {
            marsPhotosDataSource.getCuriosityPhotosBySol(sol).let {
                if (it.isSuccessful && it.body() != null) {
                    it.body()?.let { serverResponse ->
                        emit(marsPhotoApiToMarsPhotoMapper.map(serverResponse.photos.random()))
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
