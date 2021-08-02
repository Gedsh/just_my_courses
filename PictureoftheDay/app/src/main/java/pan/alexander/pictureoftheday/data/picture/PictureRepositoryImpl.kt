package pan.alexander.pictureoftheday.data.picture

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import pan.alexander.pictureoftheday.domain.picture.PictureRepository
import pan.alexander.pictureoftheday.domain.picture.entities.NasaPicture
import java.lang.RuntimeException
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PictureRepositoryImpl @Inject constructor(
    private val pictureDataSource: PictureDataSource,
    private val dateStringMapper: DateStringMapper,
    private val serverResponseToPictureMapper: ServerResponseToPictureMapper,
    private val dispatcherIO: CoroutineContext
) : PictureRepository {
    override suspend fun getPhotoByDate(date: Date): Flow<NasaPicture> {
        return flow {
            pictureDataSource.getPhotoByDate(dateStringMapper.map(date)).let {
                if (it.isSuccessful && it.body() != null) {
                    it.body()?.let { serverResponse ->
                        emit(serverResponseToPictureMapper.map(serverResponse))
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
