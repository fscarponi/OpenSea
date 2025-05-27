package it.fscarponi.opensea.domain.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location as AndroidLocation
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import it.fscarponi.opensea.domain.location.model.Location
import it.fscarponi.opensea.domain.location.util.LocationUtils
import it.fscarponi.opensea.util.AndroidLogger
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

/**
 * Android implementation of the LocationService interface.
 * Uses FusedLocationProviderClient to get location updates.
 */
class AndroidLocationService(
    private val context: Context
) : LocationService {
    private val logger = AndroidLogger("AndroidLocationService")
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    
    private val _locationFlow = MutableStateFlow<Location?>(null)
    private val locationFlow: StateFlow<Location?> = _locationFlow.asStateFlow()
    
    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
        .setMinUpdateIntervalMillis(500L)
        .setMaxUpdateDelayMillis(2000L)
        .build()
    
    private var locationCallback: LocationCallback? = null
    
    private val recentLocations = mutableListOf<Location>()
    private val recentSpeeds = mutableListOf<Double>()
    private val maxRecentLocations = 5
    private val maxRecentSpeeds = 10
    
    /**
     * Converts an Android Location to our domain Location model.
     */
    private fun AndroidLocation.toDomainLocation(): Location {
        return Location(
            latitude = latitude,
            longitude = longitude,
            altitude = altitude,
            speed = speed,
            bearing = bearing,
            accuracy = accuracy,
            timestamp = time
        )
    }
    
    @SuppressLint("MissingPermission")
    override fun startLocationUpdates() {
        if (!isLocationPermissionGranted()) {
            logger.error("Location permission not granted")
            return
        }
        
        if (locationCallback != null) {
            // Already started
            return
        }
        
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { androidLocation ->
                    val location = androidLocation.toDomainLocation()
                    _locationFlow.value = location
                    
                    // Add to recent locations for speed calculation
                    recentLocations.add(location)
                    if (recentLocations.size > maxRecentLocations) {
                        recentLocations.removeAt(0)
                    }
                    
                    // Calculate speed if we have at least two locations
                    if (recentLocations.size >= 2) {
                        val speed = if (androidLocation.hasSpeed() && androidLocation.speed > 0) {
                            // Use device-provided speed if available
                            LocationUtils.metersPerSecondToKnots(androidLocation.speed)
                        } else {
                            // Calculate speed based on distance and time
                            val prevLocation = recentLocations[recentLocations.size - 2]
                            LocationUtils.calculateSpeed(prevLocation, location)
                        }
                        
                        // Add to recent speeds for smoothing
                        recentSpeeds.add(speed)
                        if (recentSpeeds.size > maxRecentSpeeds) {
                            recentSpeeds.removeAt(0)
                        }
                    }
                }
            }
        }
        
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback!!,
                Looper.getMainLooper()
            )
            logger.debug("Location updates started")
        } catch (e: Exception) {
            logger.error("Failed to start location updates: ${e.message}")
        }
    }
    
    override fun stopLocationUpdates() {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
            locationCallback = null
            logger.debug("Location updates stopped")
        }
    }
    
    override fun getCurrentLocation(): Flow<Location> {
        return locationFlow.map { it ?: Location(0.0, 0.0, timestamp = System.currentTimeMillis()) }
    }
    
    override fun getSpeedInKnots(): Flow<Double> {
        return callbackFlow {
            val callback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { androidLocation ->
                        val speed = if (androidLocation.hasSpeed() && androidLocation.speed > 0) {
                            // Use device-provided speed if available
                            LocationUtils.metersPerSecondToKnots(androidLocation.speed)
                        } else if (recentLocations.size >= 2) {
                            // Calculate speed based on distance and time
                            val location = androidLocation.toDomainLocation()
                            val prevLocation = recentLocations[recentLocations.size - 1]
                            LocationUtils.calculateSpeed(prevLocation, location)
                        } else {
                            0.0
                        }
                        
                        // Smooth speed
                        val smoothedSpeed = if (recentSpeeds.isNotEmpty()) {
                            LocationUtils.smoothSpeed(recentSpeeds)
                        } else {
                            speed
                        }
                        
                        trySend(smoothedSpeed)
                    }
                }
            }
            
            if (isLocationPermissionGranted()) {
                try {
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        callback,
                        Looper.getMainLooper()
                    )
                } catch (e: Exception) {
                    logger.error("Failed to get speed updates: ${e.message}")
                    trySend(0.0)
                }
            } else {
                trySend(0.0)
            }
            
            awaitClose {
                fusedLocationClient.removeLocationUpdates(callback)
            }
        }
    }
    
    override fun requestLocationPermission(): Flow<Boolean> {
        return callbackFlow {
            // This would typically be handled by the Activity/Fragment
            // For this implementation, we just check if permission is already granted
            val granted = isLocationPermissionGranted()
            trySend(granted)
            awaitClose { }
        }
    }
    
    override fun isLocationPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}