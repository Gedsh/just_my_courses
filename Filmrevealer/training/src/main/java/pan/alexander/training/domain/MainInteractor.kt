package pan.alexander.training.domain

import pan.alexander.training.domain.entities.LocationWithAddress
import javax.inject.Inject

class MainInteractor @Inject constructor(
    private val contactsRepository: ContactsRepository,
    private val locationRepository: LocationRepository,
) {
    fun getAllContacts() = contactsRepository.getAllContacts()

    fun getLocationLiveData() = locationRepository.getLocationLiveData()

    suspend fun getAddressByLocation(locationWithAddress: LocationWithAddress) =
        locationRepository.getAddressByLocation(locationWithAddress)

    suspend fun getAddressByName(name: String) = locationRepository.getAddressByName(name)
}
