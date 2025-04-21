# Phase 4 (continued): Marker Visualization and Management

## Objective
Implement comprehensive marker visualization and management features to allow users to create, view, edit, and delete markers on the map.

## Tasks

### 7.1 Marker Detail UI
- Create a marker detail view that appears when a marker is selected
  ```kotlin
  @Composable
  fun MarkerDetailSheet(
      marker: Marker?,
      onClose: () -> Unit,
      onEdit: (Marker) -> Unit,
      onDelete: (Marker) -> Unit,
      modifier: Modifier = Modifier
  ) {
      if (marker == null) return
      
      BottomSheet(
          modifier = modifier,
          onDismiss = onClose
      ) {
          Column(
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
              // Marker title
              Text(
                  text = marker.title ?: "Marker",
                  style = MaterialTheme.typography.h6
              )
              
              // Marker coordinates
              Text(
                  text = "Lat: ${marker.position.latitude.format(6)}, Lon: ${marker.position.longitude.format(6)}",
                  style = MaterialTheme.typography.body2
              )
              
              // Marker description if available
              marker.description?.let {
                  Text(
                      text = it,
                      style = MaterialTheme.typography.body1
                  )
              }
              
              // Action buttons
              Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.End
              ) {
                  TextButton(onClick = { onEdit(marker) }) {
                      Text("Edit")
                  }
                  
                  Spacer(modifier = Modifier.width(8.dp))
                  
                  TextButton(onClick = { onDelete(marker) }) {
                      Text("Delete")
                  }
              }
          }
      }
  }
  ```

### 7.2 Marker Edit Dialog
- Implement a dialog for creating and editing markers
  ```kotlin
  @Composable
  fun MarkerEditDialog(
      marker: Marker?,
      onSave: (Marker) -> Unit,
      onCancel: () -> Unit
  ) {
      var title by remember { mutableStateOf(marker?.title ?: "") }
      var description by remember { mutableStateOf(marker?.description ?: "") }
      var markerType by remember { mutableStateOf(marker?.iconType ?: MarkerIconType.DEFAULT) }
      
      AlertDialog(
          onDismissRequest = onCancel,
          title = { Text(text = if (marker == null) "Add Marker" else "Edit Marker") },
          text = {
              Column {
                  // Title input
                  OutlinedTextField(
                      value = title,
                      onValueChange = { title = it },
                      label = { Text("Title") },
                      modifier = Modifier.fillMaxWidth()
                  )
                  
                  Spacer(modifier = Modifier.height(8.dp))
                  
                  // Description input
                  OutlinedTextField(
                      value = description,
                      onValueChange = { description = it },
                      label = { Text("Description") },
                      modifier = Modifier.fillMaxWidth()
                  )
                  
                  Spacer(modifier = Modifier.height(8.dp))
                  
                  // Marker type selection
                  Text("Marker Type", style = MaterialTheme.typography.subtitle1)
                  MarkerTypeSelector(
                      selectedType = markerType,
                      onTypeSelected = { markerType = it }
                  )
              }
          },
          confirmButton = {
              TextButton(
                  onClick = {
                      val updatedMarker = (marker ?: Marker(
                          id = UUID.randomUUID().toString(),
                          position = LatLng(0.0, 0.0) // Will be updated with actual position
                      )).copy(
                          title = title.takeIf { it.isNotBlank() },
                          description = description.takeIf { it.isNotBlank() },
                          iconType = markerType
                      )
                      onSave(updatedMarker)
                  }
              ) {
                  Text("Save")
              }
          },
          dismissButton = {
              TextButton(onClick = onCancel) {
                  Text("Cancel")
              }
          }
      )
  }
  ```

### 7.3 Marker Repository Implementation
- Create a repository for persistent marker storage
  ```kotlin
  interface MarkerRepository {
      suspend fun addMarker(marker: Marker)
      suspend fun updateMarker(marker: Marker)
      suspend fun deleteMarker(markerId: String)
      suspend fun getMarker(markerId: String): Marker?
      fun getAllMarkers(): Flow<List<Marker>>
  }
  
  class MarkerRepositoryImpl(
      private val markerDataSource: MarkerDataSource
  ) : MarkerRepository {
      private val _markersFlow = MutableStateFlow<List<Marker>>(emptyList())
      
      init {
          viewModelScope.launch {
              _markersFlow.value = markerDataSource.getAllMarkers()
          }
      }
      
      override suspend fun addMarker(marker: Marker) {
          markerDataSource.saveMarker(marker)
          refreshMarkers()
      }
      
      // Other method implementations
      
      private suspend fun refreshMarkers() {
          _markersFlow.value = markerDataSource.getAllMarkers()
      }
  }
  ```

### 7.4 Marker List View
- Implement a list view to display all markers
  ```kotlin
  @Composable
  fun MarkerListScreen(
      markers: List<Marker>,
      onMarkerSelected: (Marker) -> Unit,
      onClose: () -> Unit
  ) {
      Column(
          modifier = Modifier.fillMaxSize()
      ) {
          // Header with back button
          TopAppBar(
              title = { Text("Markers") },
              navigationIcon = {
                  IconButton(onClick = onClose) {
                      Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                  }
              }
          )
          
          // Marker list
          if (markers.isEmpty()) {
              Box(
                  modifier = Modifier.fillMaxSize(),
                  contentAlignment = Alignment.Center
              ) {
                  Text("No markers yet")
              }
          } else {
              LazyColumn {
                  items(markers) { marker ->
                      MarkerListItem(
                          marker = marker,
                          onClick = { onMarkerSelected(marker) }
                      )
                  }
              }
          }
      }
  }
  
  @Composable
  fun MarkerListItem(
      marker: Marker,
      onClick: () -> Unit
  ) {
      Card(
          modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 8.dp)
              .clickable(onClick = onClick),
          elevation = 2.dp
      ) {
          Row(
              modifier = Modifier.padding(16.dp),
              verticalAlignment = Alignment.CenterVertically
          ) {
              // Marker icon
              Icon(
                  imageVector = when (marker.iconType) {
                      MarkerIconType.ANCHOR -> Icons.Default.Anchor
                      MarkerIconType.WAYPOINT -> Icons.Default.Flag
                      MarkerIconType.DANGER -> Icons.Default.Warning
                      else -> Icons.Default.Place
                  },
                  contentDescription = null,
                  tint = when (marker.iconType) {
                      MarkerIconType.DANGER -> Color.Red
                      MarkerIconType.WAYPOINT -> Color.Blue
                      else -> MaterialTheme.colors.primary
                  }
              )
              
              Spacer(modifier = Modifier.width(16.dp))
              
              // Marker details
              Column {
                  Text(
                      text = marker.title ?: "Unnamed Marker",
                      style = MaterialTheme.typography.subtitle1
                  )
                  Text(
                      text = "Lat: ${marker.position.latitude.format(4)}, Lon: ${marker.position.longitude.format(4)}",
                      style = MaterialTheme.typography.caption
                  )
              }
          }
      }
  }
  ```

## Expected Outcome
A comprehensive marker management system that allows users to create, view, edit, and delete markers on the map. The system should provide a good user experience with intuitive interfaces for interacting with markers.

## Next Steps
Proceed to implementing testing and refinement features to ensure the application is robust and reliable.