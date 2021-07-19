package pan.alexander.training.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import pan.alexander.training.App.Companion.LOG_TAG
import pan.alexander.training.domain.entities.LocationWithProvider
import javax.inject.Inject

private const val REFRESH_PERIOD = 60000L
private const val MINIMAL_DISTANCE = 100f

class LocationLiveData @Inject constructor(
    private val context: Context,
    private val locationManager: LocationManager
) : LiveData<LocationWithProvider?>() {

    private var gpsProviderReady = false

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && location.provider == LocationManager.GPS_PROVIDER
            ) {
                gpsProviderReady = true
                value = LocationWithProvider(
                    location.latitude,
                    location.longitude,
                    LocationWithProvider.Provider.GPS_PROVIDER
                )
            } else if (!gpsProviderReady) {
                value = LocationWithProvider(
                    location.latitude,
                    location.longitude,
                    LocationWithProvider.Provider.NETWORK_PROVIDER
                )
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            Log.i(LOG_TAG, "Location status changed $provider")
        }

        override fun onProviderEnabled(provider: String) {
            Log.i(LOG_TAG, "Location provider enabled, provider: $provider")
        }

        override fun onProviderDisabled(provider: String) {
            if (provider == LocationManager.GPS_PROVIDER) {
                gpsProviderReady = false
            }
            Log.i(LOG_TAG, "Location provider disabled, provider: $provider")
        }
    }

    override fun onActive() {
        super.onActive()

        if (isFineLocationPermissionGranted()) {
            registerGpsLocationUpdatesListener()
        }

        if (isCoarseLocationPermissionGranted()) {
            registerNetworkLocationUpdatesListener()
        }

        getLastKnownLocationIfNecessary()
    }


    @SuppressLint("MissingPermission")
    private fun registerGpsLocationUpdatesListener() {
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            REFRESH_PERIOD,
            MINIMAL_DISTANCE,
            locationListener
        )
    }

    @SuppressLint("MissingPermission")
    private fun registerNetworkLocationUpdatesListener() {
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            REFRESH_PERIOD,
            MINIMAL_DISTANCE,
            locationListener
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocationIfNecessary() {
        if (locationManager.getProviders(true).isEmpty()) {

            var location: Location? = null
            if (isFineLocationPermissionGranted()) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }

            if (location == null && isCoarseLocationPermissionGranted()) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }

            location?.let {
                value = LocationWithProvider(
                    it.latitude,
                    it.longitude,
                    LocationWithProvider.Provider.LAST_KNOWN
                )
            }
        }
    }

    private fun isFineLocationPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isCoarseLocationPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onInactive() {
        super.onInactive()

        locationManager.removeUpdates(locationListener)
    }
}
