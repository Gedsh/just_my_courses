package pan.alexander.filmrevealer.data.preferences

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.rxjava3.RxDataStore
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import pan.alexander.filmrevealer.App.Companion.LOG_TAG
import pan.alexander.filmrevealer.domain.preferences.PreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
class PreferenceRepositoryImpl @Inject constructor(
    private val datastore: dagger.Lazy<DataStore<Preferences>>,
    private val rxDataStore: dagger.Lazy<RxDataStore<Preferences>>
) : PreferencesRepository {

    override fun getGuestSessionId(): Flow<String> {
        return getStringPreference(TMDB_GUEST_SESSION_ID)
    }

    override suspend fun setGuestSessionId(id: String) {
        datastore.get().edit { preferences ->
            preferences[TMDB_GUEST_SESSION_ID] = id
        }
    }

    override fun getGuestSessionIdRx(): Single<String> {
        return rxDataStore.get().data().flatMap {
            val id = it.asMap()[TMDB_GUEST_SESSION_ID] as? String
            if (id == null) {
                Flowable.just("")
            } else {
                Flowable.just(id)
            }
        }.first("")
    }

    override fun setGuestSessionIdRx(id: String): Completable {
        return rxDataStore.get().updateDataAsync {
            val mutablePreferences = it.toMutablePreferences()
                .apply { set(TMDB_GUEST_SESSION_ID, id) }
            Single.just(mutablePreferences)
        }.flatMapCompletable { Completable.complete() }
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
