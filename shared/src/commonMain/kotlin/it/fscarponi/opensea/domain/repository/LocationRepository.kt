package it.fscarponi.opensea.domain.repository

import it.fscarponi.opensea.domain.location.model.Location
import it.fscarponi.opensea.domain.model.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for location-related operations.
 */
interface LocationRepository : Repository {
    /**
     * Gets the current location.
     *
     * @return A Result containing the location or an error.
     */
    suspend fun getCurrentLocation(): Result<Location>

    /**
     * Gets a flow of location updates.
     *
     * @return A Flow emitting Location objects as they become available.
     */
    fun getLocationUpdates(): Flow<Location>

    /**
     * Gets a flow of speed updates in knots.
     *
     * @return A Flow emitting speed values in knots as they become available.
     */
    fun getSpeedUpdates(): Flow<Double>

    /**
     * Starts location updates.
     *
     * @param intervalMs The interval in milliseconds between location updates.
     * @return A Result indicating success or failure.
     */
    suspend fun startLocationUpdates(intervalMs: Long = 1000): Result<Unit>

    /**
     * Stops location updates.
     *
     * @return A Result indicating success or failure.
     */
    suspend fun stopLocationUpdates(): Result<Unit>

    /**
     * Requests location permission from the user.
     *
     * @return A Result indicating whether permission was granted.
     */
    suspend fun requestLocationPermission(): Result<Boolean>

    /**
     * Checks if location permission is granted.
     *
     * @return True if location permission is granted, false otherwise.
     */
    fun isLocationPermissionGranted(): Boolean
}
