package it.fscarponi.opensea.domain.repository

import it.fscarponi.opensea.domain.model.Result

/**
 * Repository interface for location-related operations.
 */
interface LocationRepository : Repository {
    /**
     * Gets the current location coordinates.
     *
     * @return A Result containing the location coordinates (latitude, longitude) or an error.
     */
    suspend fun getCurrentLocation(): Result<Pair<Double, Double>>
    
    /**
     * Starts location updates.
     *
     * @param intervalMs The interval in milliseconds between location updates.
     * @return A Result indicating success or failure.
     */
    suspend fun startLocationUpdates(intervalMs: Long): Result<Unit>
    
    /**
     * Stops location updates.
     *
     * @return A Result indicating success or failure.
     */
    suspend fun stopLocationUpdates(): Result<Unit>
}