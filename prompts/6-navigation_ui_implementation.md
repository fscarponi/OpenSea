# Phase 4: Navigation UI Implementation

## Objective
Implement the main navigation user interface using Compose Multiplatform, integrating the map and location services.

## Tasks

### 6.1 Main Navigation ViewModel
- Create the NavigationViewModel to connect location and map services
  ```kotlin
  class NavigationViewModel(
      private val locationService: LocationService,
      private val mapController: MapController,
      private val markerRepository: MarkerRepository
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
          
          viewModelScope.launch {
              markerRepository.getAllMarkers().collect { markers ->
                  _uiState.update { it.copy(markers = markers) }
              }
          }
      }
      
      fun dropMarker() {
          val location = _uiState.value.currentLocation ?: return
          val marker = Marker(
              id = UUID.randomUUID().toString(),
              position = LatLng(location.latitude, location.longitude),
              timestamp = System.currentTimeMillis()
          )
          viewModelScope.launch {
              markerRepository.addMarker(marker)
              mapController.addMarker(marker.position.latitude, marker.position.longitude)
          }
      }
      
      // Other navigation functions
  }
  
  data class NavigationUiState(
      val currentLocation: Location? = null,
      val speed: Double = 0.0,
      val markers: List<Marker> = emptyList(),
      val isFollowingLocation: Boolean = true,
      val mapMode: MapMode = MapMode.STANDARD
  )
  
  enum class MapMode {
      STANDARD, SATELLITE, NAUTICAL
  }
  ```

### 6.2 Main Map Screen
- Implement the main map screen using Compose Multiplatform
  ```kotlin
  @Composable
  fun NavigationScreen(viewModel: NavigationViewModel) {
      val uiState by viewModel.uiState.collectAsState()
      
      Box(modifier = Modifier.fillMaxSize()) {
          // Map component
          MapView(
              modifier = Modifier.fillMaxSize(),
              onMapReady = { /* Setup map */ }
          )
          
          // Speed indicator
          SpeedIndicator(
              speed = uiState.speed,
              modifier = Modifier
                  .align(Alignment.TopEnd)
                  .padding(16.dp)
          )
          
          // Control buttons
          NavigationControls(
              isFollowingLocation = uiState.isFollowingLocation,
              onFollowLocationToggle = { viewModel.toggleFollowLocation() },
              onDropMarker = { viewModel.dropMarker() },
              modifier = Modifier
                  .align(Alignment.BottomEnd)
                  .padding(16.dp)
          )
      }
  }
  ```

### 6.3 Speed Indicator Component
- Create a reusable speed indicator component
  ```kotlin
  @Composable
  fun SpeedIndicator(
      speed: Double,
      modifier: Modifier = Modifier
  ) {
      Card(
          modifier = modifier,
          elevation = 4.dp
      ) {
          Column(
              modifier = Modifier.padding(16.dp),
              horizontalAlignment = Alignment.CenterHorizontally
          ) {
              Text(
                  text = "${speed.roundToOneDecimal()}",
                  style = MaterialTheme.typography.h4
              )
              Text(
                  text = "knots",
                  style = MaterialTheme.typography.subtitle1
              )
          }
      }
  }
  
  fun Double.roundToOneDecimal(): String {
      return "%.1f".format(this)
  }
  ```

### 6.4 Navigation Controls
- Implement navigation control buttons
  ```kotlin
  @Composable
  fun NavigationControls(
      isFollowingLocation: Boolean,
      onFollowLocationToggle: () -> Unit,
      onDropMarker: () -> Unit,
      modifier: Modifier = Modifier
  ) {
      Column(
          modifier = modifier,
          verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
          // Follow location toggle button
          FloatingActionButton(
              onClick = onFollowLocationToggle,
              backgroundColor = if (isFollowingLocation) MaterialTheme.colors.primary else MaterialTheme.colors.surface
          ) {
              Icon(
                  imageVector = Icons.Default.MyLocation,
                  contentDescription = "Follow Location",
                  tint = if (isFollowingLocation) Color.White else MaterialTheme.colors.onSurface
              )
          }
          
          // Drop marker button
          FloatingActionButton(
              onClick = onDropMarker,
              backgroundColor = MaterialTheme.colors.secondary
          ) {
              Icon(
                  imageVector = Icons.Default.AddLocation,
                  contentDescription = "Drop Marker",
                  tint = Color.White
              )
          }
      }
  }
  ```

## Expected Outcome
A functional navigation UI that displays the map, shows the current speed in knots, and allows users to drop markers at their current location. The UI should be responsive and provide a good user experience on both Android and iOS platforms.

## Next Steps
Proceed to implementing marker visualization and management features.