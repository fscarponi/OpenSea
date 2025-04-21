# Phase 2: Map Integration - Basic Setup

## Objective
Integrate a mapping solution into the application and establish the foundation for displaying maps across platforms.

## Tasks

### 3.1 Map SDK Selection and Integration
- Research and select appropriate map SDK (MapLibre or Mapbox)
- Add map SDK dependencies to the project
  ```kotlin
  // For MapLibre in shared module build.gradle.kts
  implementation("org.maplibre.gl:maplibre-sdk:9.5.0")
  ```
- Configure map API keys and access tokens if required
- Set up map initialization code for both platforms

### 3.2 Common Map Interface
- Create a platform-agnostic map interface in the shared module
  ```kotlin
  // In commonMain
  interface MapController {
      fun centerOnLocation(latitude: Double, longitude: Double)
      fun addMarker(latitude: Double, longitude: Double)
      fun setZoomLevel(level: Float)
      fun getVisibleRegion(): MapRegion
  }
  
  data class MapRegion(
      val northEast: LatLng,
      val southWest: LatLng
  )
  
  data class LatLng(
      val latitude: Double,
      val longitude: Double
  )
  ```
- Define common map events and callbacks
- Create map configuration models

### 3.3 Platform-Specific Map Implementations
- Implement Android-specific MapController
  ```kotlin
  // In androidMain
  class AndroidMapController(private val mapView: MapView) : MapController {
      override fun centerOnLocation(latitude: Double, longitude: Double) {
          // Android-specific implementation
      }
      
      // Other method implementations
  }
  ```
- Implement iOS-specific MapController
  ```kotlin
  // In iosMain
  class IOSMapController(private val mapView: MKMapView) : MapController {
      override fun centerOnLocation(latitude: Double, longitude: Double) {
          // iOS-specific implementation
      }
      
      // Other method implementations
  }
  ```

### 3.4 Map Repository
- Create a MapRepository interface and implementation
- Set up methods for saving and retrieving map state
- Implement basic caching for map data

## Expected Outcome
A functioning map integration that works on both Android and iOS platforms, with a common interface for map operations that abstracts away platform-specific implementations.

## Next Steps
Proceed to implementing OpenStreetMap tile display and advanced map features.