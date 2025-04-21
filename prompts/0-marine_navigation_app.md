# Building a Marine Navigation App with Kotlin Multiplatform

## Project Plan

I'll outline a comprehensive plan for building your marine navigation app using Kotlin Multiplatform (KMM). This approach will allow you to share code between Android, iOS, and potentially desktop platforms while maintaining a clean architecture.

### 1. Project Structure

```
MarineNavigator/
├── shared/                      # Shared KMM module
│   ├── src/
│   │   ├── commonMain/          # Common code for all platforms
│   │   │   ├── domain/          # Business logic
│   │   │   ├── data/            # Data sources and repositories
│   │   │   └── presentation/    # ViewModels and state holders
│   │   ├── androidMain/         # Android-specific implementations
│   │   └── iosMain/             # iOS-specific implementations
├── androidApp/                  # Android application module
│   ├── src/
│   │   └── main/
│   │       ├── kotlin/          # Android-specific code
│   │       └── res/             # Android resources
└── iosApp/                      # iOS application
    └── iosApp/                  # iOS-specific code
```

### 2. Technology Stack

- **Kotlin Multiplatform**: For sharing code across platforms
- **Compose Multiplatform**: For UI on Android, iOS, and potentially desktop
- **Koin**: For dependency injection (lighter than Hilt and works across platforms)
- **Kotlinx Coroutines**: For asynchronous programming
- **Kotlinx Serialization**: For JSON parsing
- **Ktor**: For any network requests (if needed for map tiles)
- **Maps Integration**:
  - MapLibre or Mapbox SDK for multiplatform maps
  - OpenStreetMap as the map provider
- **Location Services**:
  - Platform-specific location APIs wrapped in a common interface

### 3. Implementation Plan

#### Phase 1: Project Setup and Basic Structure

1. Create a new KMM project
2. Set up the architecture (MVVM or MVI)
3. Configure Koin for dependency injection
4. Set up the navigation framework

#### Phase 2: Map Integration

1. Integrate MapLibre/Mapbox SDK
2. Create a common map interface
3. Implement platform-specific map views
4. Display OpenStreetMap tiles

#### Phase 3: Location Services

1. Create a location service interface
2. Implement platform-specific location providers
3. Set up real-time location updates
4. Calculate speed in knots from location data

#### Phase 4: UI Implementation

1. Create the main map screen with Compose
2. Add a speed indicator component
3. Implement a "drop marker" button
4. Design marker visualization

#### Phase 5: Testing and Refinement

1. Write unit tests for core functionality
2. Perform integration testing
3. Optimize performance
4. Polish the UI

### 4. Key Components

#### Location Service

```kotlin
// In commonMain
interface LocationService {
    fun startLocationUpdates()
    fun stopLocationUpdates()
    fun getCurrentLocation(): Flow<Location>
    fun getSpeedInKnots(): Flow<Double>
}

// In androidMain
class AndroidLocationService : LocationService {
    // Implementation using Android's LocationManager or FusedLocationProvider
}

// In iosMain
class IOSLocationService : LocationService {
    // Implementation using iOS CoreLocation
}
```

#### Map Component

```kotlin
// In commonMain
interface MapController {
    fun centerOnLocation(latitude: Double, longitude: Double)
    fun addMarker(latitude: Double, longitude: Double)
    fun setZoomLevel(level: Float)
}

// Platform-specific implementations would connect to the actual map SDKs
```

#### Main ViewModel

```kotlin
class NavigationViewModel(
    private val locationService: LocationService,
    private val mapController: MapController
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(NavigationUiState())
    val uiState: StateFlow<NavigationUiState> = _uiState.asStateFlow()
    
    init {
        viewModelScope.launch {
            locationService.getCurrentLocation().collect { location ->
                _uiState.update { it.copy(
                    currentLocation = location,
                    speed = locationService.getSpeedInKnots().first()
                ) }
                mapController.centerOnLocation(location.latitude, location.longitude)
            }
        }
    }
    
    fun dropMarker() {
        val location = _uiState.value.currentLocation ?: return
        mapController.addMarker(location.latitude, location.longitude)
        // Save marker to persistent storage if needed
    }
}

data class NavigationUiState(
    val currentLocation: Location? = null,
    val speed: Double = 0.0,
    val markers: List<Marker> = emptyList()
)
```

#### Main UI (Compose)

```kotlin
@Composable
fun NavigationScreen(viewModel: NavigationViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Map component would be rendered here
        MapView(
            modifier = Modifier.fillMaxSize(),
            onMapReady = { /* Setup map */ }
        )
        
        // Speed indicator
        Card(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text(
                text = "${uiState.speed.roundToOneDecimal()} knots",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.h6
            )
        }
        
        // Drop marker button
        FloatingActionButton(
            onClick = { viewModel.dropMarker() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.AddLocation, contentDescription = "Drop Marker")
        }
    }
}
```

### 5. Challenges and Solutions

1. **Map Integration Across Platforms**:
   - Challenge: Different map SDKs for each platform
   - Solution: Create a common interface and platform-specific implementations

2. **Location Permission Handling**:
   - Challenge: Different permission models
   - Solution: Platform-specific permission handlers with a common interface

3. **Accurate Speed Calculation**:
   - Challenge: Converting GPS data to nautical speed
   - Solution: Use the Haversine formula and time differentials between location updates

4. **Offline Map Support** (potential future feature):
   - Challenge: Storing map tiles for offline use
   - Solution: Implement a tile caching system

### 6. Next Steps and Future Enhancements

1. Add route planning functionality
2. Implement weather data integration
3. Add depth information (if data is available)
4. Create a logbook for tracking journeys
5. Add support for importing/exporting waypoints

This plan provides a solid foundation for your marine navigation app. The modular architecture will make it easy to add features as you go, while the Kotlin Multiplatform approach ensures you can maintain a single codebase for multiple platforms.