package it.fscarponi.opensea.domain.location

import it.fscarponi.opensea.domain.location.model.Location
import kotlinx.coroutines.flow.Flow

/**
 * Interface for location services across platforms.
 * Provides methods for tracking device location and calculating navigation metrics.
 */
interface LocationService {
    /**
     * Starts location updates.
     * This will begin tracking the device's location at the configured frequency.
     */
    fun startLocationUpdates()
    
    /**
     * Stops location updates.
     * This will stop tracking the device's location to conserve battery.
     */
    fun stopLocationUpdates()
    
    /**
     * Gets a flow of the current location.
     * 
     * @return A Flow emitting Location objects as they become available.
     */
    fun getCurrentLocation(): Flow<Location>
    
    /**
     * Gets a flow of the current speed in knots.
     * 
     * @return A Flow emitting speed values in knots as they become available.
     */
    fun getSpeedInKnots(): Flow<Double>
    
    /**
     * Requests location permission from the user.
     * 
     * @return A Flow emitting a boolean indicating whether permission was granted.
     */
    fun requestLocationPermission(): Flow<Boolean>
    
    /**
     * Checks if location permission is granted.
     * 
     * @return True if location permission is granted, false otherwise.
     */
    fun isLocationPermissionGranted(): Boolean
}