package pan.alexander.filmrevealer.domain.preferences

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun getGuestSessionId(): Flow<String>
    suspend fun setGuestSessionId(id: String)
    @ExperimentalCoroutinesApi
    fun getGuestSessionIdRx(): Single<String>
    @ExperimentalCoroutinesApi
    fun setGuestSessionIdRx(id: String): Completable
}
