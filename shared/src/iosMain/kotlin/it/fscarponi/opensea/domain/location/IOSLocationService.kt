package it.fscarponi.opensea.domain.location

import it.fscarponi.opensea.domain.location.model.Location
import it.fscarponi.opensea.util.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

/**
 * iOS implementation of the LocationService interface.
 * 
 * Note: This is a placeholder implementation. In a real application, this would use
 * CLLocationManager to get location updates from the device. Due to the complexity
 * of Kotlin/Native interop with iOS frameworks, this implementation provides simulated
 * data for demonstration purposes.
 */
class IOSLocationService : LocationService {
    private val logger = Logger.instance
    private val TAG = "IOSLocationService"

    private val _locationFlow = MutableStateFlow<Location?>(null)

    // Simulated location for demonstration purposes
    private val simulatedLocation = Location(
        latitude = 40.7128, // New York City
        longitude = -74.0060,
        altitude = 10.0,
        speed = 5.0f,
        bearing = 90.0f,
        accuracy = 10.0f,
        timestamp = getCurrentTimeMillis()
    )

    init {
        logger.debug(TAG, "Initializing IOSLocationService")
        _locationFlow.value = simulatedLocation
    }

    /**
     * Gets the current time in milliseconds since epoch.
     */
    private fun getCurrentTimeMillis(): Long {
        return (NSDate().timeIntervalSince1970 * 1000).toLong()
    }

    override fun startLocationUpdates() {
        logger.debug(TAG, "Location updates started (simulated)")
        // In a real implementation, this would start CLLocationManager updates
    }

    override fun stopLocationUpdates() {
        logger.debug(TAG, "Location updates stopped (simulated)")
        // In a real implementation, this would stop CLLocationManager updates
    }

    override fun getCurrentLocation(): Flow<Location> {
        return _locationFlow.map { it ?: simulatedLocation.copy(timestamp = getCurrentTimeMillis()) }
    }

    override fun getSpeedInKnots(): Flow<Double> {
        // Convert m/s to knots (1 m/s = 1.94384 knots)
        val speedInKnots = (simulatedLocation.speed ?: 0.0f) * 1.94384
        return flowOf(speedInKnots.toDouble())
    }

    override fun requestLocationPermission(): Flow<Boolean> {
        logger.debug(TAG, "Requesting location permission (simulated)")
        // In a real implementation, this would request permission using CLLocationManager
        return flowOf(true)
    }

    override fun isLocationPermissionGranted(): Boolean {
        // In a real implementation, this would check CLLocationManager authorization status
        return true
    }
}
