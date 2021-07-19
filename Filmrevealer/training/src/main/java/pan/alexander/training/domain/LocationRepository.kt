package pan.alexander.training.domain

import androidx.lifecycle.LiveData
import pan.alexander.training.domain.entities.LocationWithAddress
import pan.alexander.training.domain.entities.LocationWithProvider

interface LocationRepository {
    fun getLocationLiveData(): LiveData<LocationWithProvider?>
    suspend fun getAddressByLocation(locationWithAddress: LocationWithAddress): LocationWithAddress
    suspend fun getAddressByName(name: String): LocationWithAddress?
}
