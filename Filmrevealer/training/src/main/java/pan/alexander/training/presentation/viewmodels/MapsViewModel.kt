package pan.alexander.training.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pan.alexander.training.App
import pan.alexander.training.domain.entities.LocationWithAddress
import pan.alexander.training.domain.entities.LocationWithProvider

private const val DEFAULT_LATITUDE = -34.0
private const val DEFAULT_LONGITUDE = 151.0
private const val DEFAULT_ADDRESS = "Sydney"

class MapsViewModel : ViewModel() {
    private val mainInteractor = App.instance.daggerComponent.getMainInteractor()

    private val addressByLocationLiveData by lazy {
        MutableLiveData<LocationWithAddress>().apply {
            value = LocationWithAddress(
                latitude = DEFAULT_LATITUDE,
                longitude = DEFAULT_LONGITUDE,
                addressLine = DEFAULT_ADDRESS,
                destination = LocationWithAddress.Destination.SELECTED_LOCATION
            )
        }
    }
    private val addressByNameLiveData by lazy { MutableLiveData<LocationWithAddress>() }

    fun getLocationLiveData(): LiveData<LocationWithProvider?> =
        mainInteractor.get().getLocationLiveData()

    fun getAddressByLocationLiveData(): LiveData<LocationWithAddress> = addressByLocationLiveData

    fun getAddressByNameLiveData(): LiveData<LocationWithAddress> = addressByNameLiveData

    fun getAddressByLocation(locationWithAddress: LocationWithAddress) = viewModelScope.launch {
        addressByLocationLiveData.value =
            mainInteractor.get().getAddressByLocation(locationWithAddress)
    }

    fun getAddressByName(name: String) = viewModelScope.launch {
        mainInteractor.get().getAddressByName(name)?.let { addressByNameLiveData.value = it }
    }
}
