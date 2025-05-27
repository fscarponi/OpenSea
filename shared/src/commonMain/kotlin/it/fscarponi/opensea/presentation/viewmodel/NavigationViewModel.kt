package it.fscarponi.opensea.presentation.viewmodel

import it.fscarponi.opensea.domain.location.LocationService
import it.fscarponi.opensea.domain.location.model.Location
import it.fscarponi.opensea.domain.map.MapController
import it.fscarponi.opensea.domain.map.model.LatLng
import it.fscarponi.opensea.domain.map.model.Marker
import it.fscarponi.opensea.domain.map.model.MarkerIconType
import it.fscarponi.opensea.domain.repository.MarkerRepository
import it.fscarponi.opensea.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * ViewModel for the navigation screen.
 * Connects location and map services to provide navigation functionality.
 */
class NavigationViewModel(
    private val locationService: LocationService,
    private val mapController: MapController,
    private val markerRepository: MarkerRepository
) : BaseViewModel<NavigationUiState>(NavigationUiState()) {

    init {
        // Start location updates and collect current location
        viewModelScope.launch {
            locationService.getCurrentLocation().collect { location ->
                val speed = locationService.getSpeedInKnots().first()
                updateState { it.copy(
                    currentLocation = location,
                    speed = speed
                ) }

                // Center map on current location if following location is enabled
                if (state.value.isFollowingLocation) {
                    mapController.centerOnLocation(location.latitude, location.longitude)
                }
            }
        }

        // Collect markers
        viewModelScope.launch {
            markerRepository.getAllMarkers().collect { markers ->
                updateState { it.copy(markers = markers) }
            }
        }
    }

    /**
     * Toggles whether the map should follow the user's location.
     */
    fun toggleFollowLocation() {
        updateState { it.copy(isFollowingLocation = !it.isFollowingLocation) }

        // If following location is now enabled, center map on current location
        if (state.value.isFollowingLocation) {
            state.value.currentLocation?.let { location ->
                mapController.centerOnLocation(location.latitude, location.longitude)
            }
        }
    }

    /**
     * Drops a marker at the current location.
     */
    fun dropMarker() {
        val location = state.value.currentLocation ?: return
        val marker = Marker(
            id = generateUniqueId(),
            position = LatLng(location.latitude, location.longitude),
            title = "Marker ${state.value.markers.size + 1}",
            description = "Created at current location",
            iconType = MarkerIconType.DEFAULT
        )

        viewModelScope.launch {
            markerRepository.addMarker(marker)
            mapController.addMarker(marker.position.latitude, marker.position.longitude)
        }
    }

    /**
     * Generates a unique ID for markers.
     */
    private fun generateUniqueId(): String {
        val randomBytes = ByteArray(16)
        Random.nextBytes(randomBytes)
        return randomBytes.joinToString("") { byte -> 
            val hexChars = "0123456789abcdef"
            val i = byte.toInt() and 0xFF
            "${hexChars[i shr 4]}${hexChars[i and 0x0F]}"
        }
    }

    /**
     * Changes the map mode.
     *
     * @param mode The new map mode.
     */
    fun setMapMode(mode: MapMode) {
        updateState { it.copy(mapMode = mode) }

        // Update map style based on mode
        when (mode) {
            MapMode.STANDARD -> {
                mapController.setNightMode(false)
                mapController.setMarineStyle(false)
            }
            MapMode.SATELLITE -> {
                mapController.setNightMode(false)
                mapController.setMarineStyle(false)
                // Note: Satellite view would typically require additional platform-specific implementation
            }
            MapMode.NAUTICAL -> {
                mapController.setMarineStyle(true)
            }
        }
    }
}

/**
 * UI state for the navigation screen.
 */
data class NavigationUiState(
    val currentLocation: Location? = null,
    val speed: Double = 0.0,
    val markers: List<Marker> = emptyList(),
    val isFollowingLocation: Boolean = true,
    val mapMode: MapMode = MapMode.STANDARD
)

/**
 * Enum representing different map modes.
 */
enum class MapMode {
    STANDARD, SATELLITE, NAUTICAL
}
