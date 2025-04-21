# Phase 2: Map Integration - Basic Setup (Result)

## Implementation Summary

This phase focused on integrating a mapping solution into the application and establishing the foundation for displaying maps across platforms. The following tasks were completed:

### 3.1 Map SDK Selection and Integration
- Selected MapLibre as the map SDK for Android
- Added MapLibre SDK dependency to the shared module build.gradle.kts
- For iOS, prepared for MapKit integration with a simplified implementation

### 3.2 Common Map Interface
- Created a platform-agnostic map interface in the shared module
- Implemented the following data classes and interfaces:
  - `LatLng`: Represents geographical coordinates
  - `MapRegion`: Represents a rectangular region on a map
  - `MapConfiguration`: Provides configuration options for maps
  - `MapController`: Interface for common map operations
  - `MapEvent`: Sealed class for map-related events

### 3.3 Platform-Specific Map Implementations
- Implemented `AndroidMapController` using MapLibre SDK
- Created a simplified `IOSMapController` with proper annotations
- Both implementations follow the common `MapController` interface

### 3.4 Map Repository
- Created `MapRepository` interface for map state management
- Implemented `MapRepositoryImpl` with in-memory caching
- Added methods for saving and retrieving map state
- Updated Koin module to include the map repository

## Code Structure

```
shared/src/
├── commonMain/kotlin/it/fscarponi/opensea/
│   ├── domain/map/
│   │   ├── MapController.kt
│   │   ├── event/
│   │   │   └── MapEvent.kt
│   │   └── model/
│   │       ├── LatLng.kt
│   │       ├── MapRegion.kt
│   │       └── MapConfiguration.kt
│   ├── domain/repository/
│   │   └── MapRepository.kt
│   ├── data/repository/
│   │   └── MapRepositoryImpl.kt
│   └── di/
│       └── KoinModule.kt (updated)
├── androidMain/kotlin/it/fscarponi/opensea/
│   └── domain/map/
│       └── AndroidMapController.kt
└── iosMain/kotlin/it/fscarponi/opensea/
    └── domain/map/
        └── IOSMapController.kt
```

## Next Steps
- Implement OpenStreetMap tile display
- Add advanced map features like custom markers and overlays
- Enhance the iOS implementation with full MapKit functionality
- Add persistence to the map repository