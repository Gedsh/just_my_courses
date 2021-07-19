package pan.alexander.filmrevealer.domain

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun getGuestSessionId(): Flow<String>
    suspend fun setGuestSessionId(id: String)
}
