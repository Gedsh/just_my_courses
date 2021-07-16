package pan.alexander.training.presentation.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import pan.alexander.training.R
import pan.alexander.training.databinding.FragmentMapsBinding
import pan.alexander.training.domain.entities.LocationWithAddress
import pan.alexander.training.presentation.viewmodels.MapsViewModel
import pan.alexander.training.utils.PermissionUtils

private const val DEFAULT_ZOOM = 15f

class MapsFragment : Fragment(), OnMapReadyCallback {

    private val viewModel by lazy { ViewModelProvider(this).get(MapsViewModel::class.java) }
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private var searchView: SearchView? = null
    private lateinit var map: GoogleMap

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrElse(Manifest.permission.ACCESS_FINE_LOCATION, { false }) -> {
                observeLocationChanges()
                activateMyLocation(true)
            }
            permissions.getOrElse(Manifest.permission.ACCESS_COARSE_LOCATION, { false }) -> {
                observeLocationChanges()
                activateMyLocation(true)
            }
            else -> {
                showLocationPermissionMissingDialog()
                activateMyLocation(false)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.maps_menu, menu)
        initSearch(menu)
    }

    private fun initSearch(menu: Menu) {
        searchView = menu.findItem(R.id.mainMenuSearch)?.actionView as? SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.takeIf { it.isNotEmpty() }?.let {
                    viewModel.getAddressByName(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        googleMap.uiSettings.isZoomControlsEnabled = true

        checkLocationPermissions()

        setMapOnLongClickListener(googleMap)

        observeAddressByLocationLiveData()

        observeAddressByNameLiveData()
    }

    private fun setMapOnLongClickListener(googleMap: GoogleMap) {
        googleMap.setOnMapLongClickListener { latlng ->
            searchView?.let {
                it.setQuery("", false)
                it.isIconified = true
            }

            setMarker(latlng, "")

            viewModel.getAddressByLocation(
                LocationWithAddress(
                    latitude = latlng.latitude,
                    longitude = latlng.longitude,
                    addressLine = "",
                    destination = LocationWithAddress.Destination.SELECTED_LOCATION
                )
            )
        }
    }

    private fun checkLocationPermissions() {
        activity?.let { activity ->
            PermissionUtils.checkLocationPermission(
                activity,
                locationPermissionRequest
            ) {
                when (it) {
                    PermissionUtils.LocationPermission.ACCESS_FINE_LOCATION -> {
                        observeLocationChanges()
                        activateMyLocation(true)
                    }
                    PermissionUtils.LocationPermission.ACCESS_COARSE_LOCATION -> {
                        observeLocationChanges()
                        activateMyLocation(true)
                    }
                    PermissionUtils.LocationPermission.NO_PERMISSION -> {
                        showLocationPermissionMissingDialog()
                        activateMyLocation(false)
                    }
                }
            }
        }
    }

    private fun showLocationPermissionMissingDialog() {
        context?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.access_to_location_dialog_title)
                .setMessage(R.string.access_to_location_missing_dialog_message)
                .setNegativeButton(R.string.close) { _, _ ->
                }.show()
        }
    }

    private fun observeLocationChanges() {
        viewModel.getLocationLiveData().observe(viewLifecycleOwner) { locationWithProvider ->
            locationWithProvider?.let {
                viewModel.getAddressByLocation(
                    LocationWithAddress(
                        latitude = locationWithProvider.latitude,
                        longitude = locationWithProvider.longitude,
                        addressLine = "",
                        destination = LocationWithAddress.Destination.CURRENT_LOCATION
                    )
                )
            }
        }
    }

    private fun observeAddressByLocationLiveData() {
        viewModel.getAddressByLocationLiveData()
            .observe(viewLifecycleOwner) { locationWithAddress ->
                when (locationWithAddress.destination) {
                    LocationWithAddress.Destination.CURRENT_LOCATION -> setCurrentLocation(
                        locationWithAddress
                    )
                    LocationWithAddress.Destination.SELECTED_LOCATION -> {
                        locationWithAddress.takeIf { it.addressLine.isNotBlank() }?.let {
                            setSelectedLocation(it)
                            setMarker(LatLng(it.latitude, it.longitude), it.addressLine)
                        }
                    }
                }
            }
    }

    private fun observeAddressByNameLiveData() {
        viewModel.getAddressByNameLiveData().observe(viewLifecycleOwner) {
            goToAddress(it)
            setSelectedLocation(it)
        }
    }

    private fun setCurrentLocation(locationWithAddress: LocationWithAddress) = with(binding) {
        if (groupCurrentLocation.visibility == View.GONE) {
            groupCurrentLocation.visibility = View.VISIBLE
        }
        textViewCurrentLocationLatValue.text = String.format("%.2f", locationWithAddress.latitude)
        textViewCurrentLocationLonValue.text = String.format("%.2f", locationWithAddress.longitude)
        textViewCurrentLocationAddressValue.text = locationWithAddress.addressLine
    }

    private fun setSelectedLocation(locationWithAddress: LocationWithAddress) = with(binding) {
        if (groupSelectedLocation.visibility == View.GONE) {
            groupSelectedLocation.visibility = View.VISIBLE
        }
        textViewSelectedLocationLatValue.text = String.format("%.2f", locationWithAddress.latitude)
        textViewSelectedLocationLonValue.text = String.format("%.2f", locationWithAddress.longitude)
        textViewSelectedLocationAddressValue.text = locationWithAddress.addressLine
    }

    private fun goToAddress(
        locationWithAddress: LocationWithAddress
    ) {
        val location = LatLng(
            locationWithAddress.latitude,
            locationWithAddress.longitude
        )
        setMarker(location, locationWithAddress.addressLine)
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                location,
                DEFAULT_ZOOM
            )
        )
    }

    private fun setMarker(
        location: LatLng,
        title: String?,
    ): Marker? {

        map.clear()

        return map.addMarker(
            MarkerOptions().position(location).title(title)
        )
    }

    @SuppressLint("MissingPermission")
    private fun activateMyLocation(activate: Boolean) {
        map.isMyLocationEnabled = activate
        map.uiSettings.isMyLocationButtonEnabled = activate
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
