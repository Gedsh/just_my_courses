package pan.alexander.filmrevealer.data

import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import pan.alexander.filmrevealer.App
import pan.alexander.filmrevealer.App.Companion.LOG_TAG
import pan.alexander.filmrevealer.domain.PreferencesRepository

class PreferenceRepositoryImplementation : PreferencesRepository {
    val datastore = App.instance.daggerComponent.getDataStore()

    override fun getGuestSessionId(): Flow<String> {
        return getStringPreference(TMDB_GUEST_SESSION_ID)
    }

    override suspend fun setGuestSessionId(id: String) {
        datastore.get().edit { preferences ->
            preferences[TMDB_GUEST_SESSION_ID] = id
        }
    }


    private fun getStringPreference(key: Preferences.Key<String>): Flow<String> {
        return datastore.get().data
            .catch { exception ->
                Log.e(LOG_TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[key] ?: ""
            }
    }
}
