package it.fscarponi.opensea.data.repository

import it.fscarponi.opensea.domain.location.LocationService
import it.fscarponi.opensea.domain.location.model.Location
import it.fscarponi.opensea.domain.model.Result
import it.fscarponi.opensea.domain.repository.LocationRepository
import it.fscarponi.opensea.util.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Implementation of the LocationRepository interface.
 * Uses the LocationService to get location data.
 */
class LocationRepositoryImpl(
    private val locationService: LocationService
) : LocationRepository {
    private val logger = Logger.instance
    private val TAG = "LocationRepositoryImpl"
    
    override suspend fun getCurrentLocation(): Result<Location> {
        return try {
            val location = locationService.getCurrentLocation().first()
            Result.Success(location)
        } catch (e: Exception) {
            logger.error(TAG, "Error getting current location", e)
            Result.Error("Failed to get current location: ${e.message}", e)
        }
    }
    
    override fun getLocationUpdates(): Flow<Location> {
        return locationService.getCurrentLocation()
            .catch { e ->
                logger.error(TAG, "Error in location updates", e)
                throw e
            }
    }
    
    override fun getSpeedUpdates(): Flow<Double> {
        return locationService.getSpeedInKnots()
            .catch { e ->
                logger.error(TAG, "Error in speed updates", e)
                throw e
            }
    }
    
    override suspend fun startLocationUpdates(intervalMs: Long): Result<Unit> {
        return try {
            locationService.startLocationUpdates()
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.error(TAG, "Error starting location updates", e)
            Result.Error("Failed to start location updates: ${e.message}", e)
        }
    }
    
    override suspend fun stopLocationUpdates(): Result<Unit> {
        return try {
            locationService.stopLocationUpdates()
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.error(TAG, "Error stopping location updates", e)
            Result.Error("Failed to stop location updates: ${e.message}", e)
        }
    }
    
    override suspend fun requestLocationPermission(): Result<Boolean> {
        return try {
            val granted = locationService.requestLocationPermission().first()
            Result.Success(granted)
        } catch (e: Exception) {
            logger.error(TAG, "Error requesting location permission", e)
            Result.Error("Failed to request location permission: ${e.message}", e)
        }
    }
    
    override fun isLocationPermissionGranted(): Boolean {
        return locationService.isLocationPermissionGranted()
    }
}