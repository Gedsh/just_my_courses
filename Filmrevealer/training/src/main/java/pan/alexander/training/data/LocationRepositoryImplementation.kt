package pan.alexander.training.data

import androidx.lifecycle.LiveData
import pan.alexander.training.domain.LocationRepository
import pan.alexander.training.domain.entities.LocationWithAddress
import pan.alexander.training.domain.entities.LocationWithProvider
import javax.inject.Inject

class LocationRepositoryImplementation @Inject constructor(
    private val locationLiveData: dagger.Lazy<LocationLiveData>,
    private val locationDao: LocationDao
) : LocationRepository {
    override fun getLocationLiveData(): LiveData<LocationWithProvider?> = locationLiveData.get()

    override suspend fun getAddressByLocation(
        locationWithAddress: LocationWithAddress
    ): LocationWithAddress = locationDao.getAddressByLocation(locationWithAddress)

    override suspend fun getAddressByName(name: String): LocationWithAddress? =
        locationDao.getAddressByName(name)
}
