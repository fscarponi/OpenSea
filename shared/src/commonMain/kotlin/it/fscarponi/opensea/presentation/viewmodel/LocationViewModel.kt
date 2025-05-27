package it.fscarponi.opensea.presentation.viewmodel

import it.fscarponi.opensea.domain.location.model.Location
import it.fscarponi.opensea.domain.model.Result
import it.fscarponi.opensea.domain.repository.LocationRepository
import it.fscarponi.opensea.domain.usecase.GetCurrentLocationUseCase
import it.fscarponi.opensea.presentation.base.BaseViewModel
import it.fscarponi.opensea.presentation.model.DataUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * ViewModel for location-related functionality.
 */
class LocationViewModel(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val locationRepository: LocationRepository
) : BaseViewModel<DataUiState<Location>>(DataUiState.loading()) {

    private val _speedFlow = MutableStateFlow<Double>(0.0)
    val speedFlow: StateFlow<Double> = _speedFlow.asStateFlow()

    private val _locationUpdatesFlow = MutableStateFlow<Location?>(null)
    val locationUpdatesFlow: StateFlow<Location?> = _locationUpdatesFlow.asStateFlow()

    private val _permissionGrantedFlow = MutableStateFlow<Boolean?>(null)
    val permissionGrantedFlow: StateFlow<Boolean?> = _permissionGrantedFlow.asStateFlow()

    init {
        checkLocationPermission()
    }

    /**
     * Checks if location permission is granted.
     */
    private fun checkLocationPermission() {
        _permissionGrantedFlow.value = locationRepository.isLocationPermissionGranted()
    }

    /**
     * Requests location permission from the user.
     */
    fun requestLocationPermission() {
        viewModelScope.launch {
            when (val result = locationRepository.requestLocationPermission()) {
                is Result.Success -> {
                    _permissionGrantedFlow.value = result.data
                    if (result.data) {
                        startLocationUpdates()
                    }
                }
                is Result.Error -> {
                    _permissionGrantedFlow.value = false
                }
                is Result.Loading -> {
                    // Do nothing
                }
            }
        }
    }

    /**
     * Gets the current location.
     */
    fun getCurrentLocation() {
        updateState { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                when (val result = getCurrentLocationUseCase()) {
                    is Result.Success -> {
                        updateState { DataUiState.success(result.data) }
                    }
                    is Result.Error -> {
                        updateState { DataUiState.error(result.message) }
                    }
                    is Result.Loading -> {
                        // Already handled by initial state update
                    }
                }
            } catch (e: Exception) {
                updateState { DataUiState.error(e.message ?: "Unknown error") }
            }
        }
    }

    /**
     * Starts location updates.
     */
    fun startLocationUpdates() {
        viewModelScope.launch {
            locationRepository.startLocationUpdates()

            // Collect location updates
            locationRepository.getLocationUpdates()
                .onEach { location ->
                    _locationUpdatesFlow.value = location
                }
                .launchIn(viewModelScope)

            // Collect speed updates
            locationRepository.getSpeedUpdates()
                .onEach { speed ->
                    _speedFlow.value = speed
                }
                .launchIn(viewModelScope)
        }
    }

    /**
     * Stops location updates.
     */
    fun stopLocationUpdates() {
        viewModelScope.launch {
            locationRepository.stopLocationUpdates()
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
    }
}
