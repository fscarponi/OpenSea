# Phase 3: Location Services Implementation

## Objective
Implement location services to track the device's position in real-time and calculate navigation metrics like speed in knots.

## Tasks

### 5.1 Location Service Interface
- Create a common location service interface in the shared module
  ```kotlin
  // In commonMain
  interface LocationService {
      fun startLocationUpdates()
      fun stopLocationUpdates()
      fun getCurrentLocation(): Flow<Location>
      fun getSpeedInKnots(): Flow<Double>
      fun requestLocationPermission(): Flow<Boolean>
      fun isLocationPermissionGranted(): Boolean
  }
  
  data class Location(
      val latitude: Double,
      val longitude: Double,
      val altitude: Double? = null,
      val speed: Float? = null,
      val bearing: Float? = null,
      val accuracy: Float? = null,
      val timestamp: Long
  )
  ```
- Define location update frequency and accuracy requirements
- Create utility functions for location calculations

### 5.2 Android Location Implementation
- Implement Android-specific location service
  ```kotlin
  // In androidMain
  class AndroidLocationService(
      private val context: Context
  ) : LocationService {
      private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
      private val _locationFlow = MutableStateFlow<Location?>(null)
      
      override fun startLocationUpdates() {
          // Request location updates using FusedLocationProviderClient
      }
      
      // Other method implementations
  }
  ```
- Handle Android location permissions
- Configure location request parameters for marine use case
- Implement background location updates if needed

### 5.3 iOS Location Implementation
- Implement iOS-specific location service
  ```kotlin
  // In iosMain
  class IOSLocationService : LocationService {
      private val locationManager = CLLocationManager()
      private val _locationFlow = MutableStateFlow<Location?>(null)
      
      override fun startLocationUpdates() {
          // Configure and start CLLocationManager updates
      }
      
      // Other method implementations
  }
  ```
- Handle iOS location permissions
- Configure location accuracy and update frequency
- Implement background location updates if needed

### 5.4 Speed Calculation
- Implement speed calculation in knots
  ```kotlin
  // Convert meters per second to knots
  fun Float.toKnots(): Double = this * 1.94384
  
  // Calculate speed between two points using time difference
  fun calculateSpeed(location1: Location, location2: Location): Double {
      val distance = calculateDistance(
          location1.latitude, location1.longitude,
          location2.latitude, location2.longitude
      )
      val timeDiff = (location2.timestamp - location1.timestamp) / 1000.0 // seconds
      val speedMps = distance / timeDiff
      return speedMps.toKnots()
  }
  
  // Haversine formula for distance calculation
  fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
      // Implementation of Haversine formula
  }
  ```
- Implement smoothing algorithm for speed readings
- Add unit conversion utilities (knots, km/h, mph)

## Expected Outcome
A fully functional location service that works across platforms, providing real-time location updates and accurate speed calculations in nautical units.

## Next Steps
Proceed to integrating the location service with the map component and implementing the main navigation UI.