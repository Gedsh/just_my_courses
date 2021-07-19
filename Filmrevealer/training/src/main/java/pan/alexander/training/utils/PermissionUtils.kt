package pan.alexander.training.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import pan.alexander.training.R

object PermissionUtils {

    fun checkReadContactsPermission(
        activity: Activity?,
        requestPermissionLauncher: ActivityResultLauncher<String>,
        block: (granted: Boolean) -> Unit
    ) {

        activity?.let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    block(true)
                }

                shouldShowRequestPermissionRationale(it, Manifest.permission.READ_CONTACTS) -> {
                    showReadContactsPermissionRationaleDialog(it, requestPermissionLauncher, block)
                }

                else -> {
                    requestReadContactsPermission(requestPermissionLauncher)
                }
            }
        }
    }

    private fun requestReadContactsPermission(requestPermissionLauncher: ActivityResultLauncher<String>) {
        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
    }

    private fun showReadContactsPermissionRationaleDialog(
        activity: Activity,
        requestPermissionLauncher: ActivityResultLauncher<String>,
        block: (granted: Boolean) -> Unit
    ) {
        AlertDialog.Builder(activity)
            .setTitle(R.string.access_to_contacts_dialog_title)
            .setMessage(R.string.access_to_contacts_rationale_dialog_message)
            .setPositiveButton(R.string.allow) { _, _ ->
                requestReadContactsPermission(requestPermissionLauncher)
            }
            .setNegativeButton(R.string.deny) { _, _ ->
                block(false)
            }.show()
    }

    fun checkLocationPermission(
        activity: Activity?,
        locationPermissionRequest: ActivityResultLauncher<Array<out String>>,
        block: (permission: LocationPermission) -> Unit
    ) {

        activity?.let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    block(LocationPermission.ACCESS_FINE_LOCATION)
                }

                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    block(LocationPermission.ACCESS_COARSE_LOCATION)
                }

                shouldShowRequestPermissionRationale(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    showLocationPermissionRationaleDialog(it, locationPermissionRequest, block)
                }

                shouldShowRequestPermissionRationale(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) -> {
                    showLocationPermissionRationaleDialog(it, locationPermissionRequest, block)
                }

                else -> {
                    requestLocationPermission(locationPermissionRequest)
                }
            }
        }
    }

    private fun requestLocationPermission(
        requestPermissionLauncher: ActivityResultLauncher<Array<out String>>
    ) {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun showLocationPermissionRationaleDialog(
        activity: Activity,
        requestPermissionLauncher: ActivityResultLauncher<Array<out String>>,
        block: (permission: LocationPermission) -> Unit
    ) {
        AlertDialog.Builder(activity)
            .setTitle(R.string.access_to_location_dialog_title)
            .setMessage(R.string.access_to_location_rationale_dialog_message)
            .setPositiveButton(R.string.allow) { _, _ ->
                requestLocationPermission(requestPermissionLauncher)
            }
            .setNegativeButton(R.string.deny) { _, _ ->
                block(LocationPermission.NO_PERMISSION)
            }.show()
    }

    enum class LocationPermission {
        NO_PERMISSION,
        ACCESS_FINE_LOCATION,
        ACCESS_COARSE_LOCATION
    }
}
