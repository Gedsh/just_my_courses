package pan.alexander.training.data

import android.location.Geocoder
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import pan.alexander.training.App.Companion.LOG_TAG
import pan.alexander.training.domain.entities.LocationWithAddress
import javax.inject.Inject

class LocationDao @Inject constructor(
    private val dispatcherIO: CoroutineDispatcher,
    private val geoCoder: dagger.Lazy<Geocoder>
) {
    suspend fun getAddressByLocation(locationWithAddress: LocationWithAddress): LocationWithAddress {
        return withContext(dispatcherIO) {
            val addresses = mutableListOf<android.location.Address>()
            kotlin.runCatching {
                geoCoder.get().getFromLocation(
                    locationWithAddress.latitude,
                    locationWithAddress.longitude,
                    1
                )
            }.onSuccess {
                addresses.addAll(it)
            }.onFailure {
                Log.e(LOG_TAG, "Get address by location failure", it)
            }

            val addressLine = addresses.firstOrNull()?.getAddressLine(0) ?: "N/A"

            locationWithAddress.also { it.addressLine = addressLine }
        }
    }

    suspend fun getAddressByName(name: String): LocationWithAddress? {
        return withContext(dispatcherIO) {
            val addresses = mutableListOf<android.location.Address>()
            kotlin.runCatching {
                geoCoder.get().getFromLocationName(name, 1)
            }.onSuccess {
                addresses.addAll(it)
            }.onFailure {
                Log.e(LOG_TAG, "Get address by name failure", it)
            }
            val address = addresses.firstOrNull()
            var locationWithAddress: LocationWithAddress? = null
            address?.let {
                locationWithAddress = LocationWithAddress(
                    latitude = it.latitude,
                    longitude = it.longitude,
                    addressLine = it.getAddressLine(0) ?: "N/A",
                    destination = LocationWithAddress.Destination.SELECTED_LOCATION
                )
            }
            locationWithAddress
        }
    }
}
