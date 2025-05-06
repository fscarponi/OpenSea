# OpenSea - Marine Navigation App

OpenSea is a cross-platform marine navigation application built with Kotlin Multiplatform Mobile (KMM). It provides sailors and marine enthusiasts with essential navigation tools, real-time location tracking, and map visualization capabilities.

## Features

- Cross-platform support for Android and iOS
- Real-time location tracking with speed calculation in knots
- Interactive map display using MapLibre (Android) and MapKit (iOS)
- Marker placement for navigation waypoints
- Clean architecture with separation of concerns

## Technology Stack

- **Kotlin Multiplatform Mobile (KMM)**: For sharing code across platforms
- **Compose Multiplatform**: For UI components
- **Koin**: For dependency injection
- **Kotlinx Coroutines**: For asynchronous programming
- **MapLibre**: For Android map integration
- **MapKit**: For iOS map integration
- **OpenStreetMap**: As the map data provider

## Project Structure

```
OpenSea/
├── shared/                      # Shared KMM module
│   ├── src/
│   │   ├── commonMain/          # Common code for all platforms
│   │   │   ├── domain/          # Business logic and interfaces
│   │   │   ├── data/            # Data sources and repositories
│   │   │   └── presentation/    # ViewModels and state holders
│   │   ├── androidMain/         # Android-specific implementations
│   │   └── iosMain/             # iOS-specific implementations
├── androidApp/                  # Android application module
└── prompts/                     # Project documentation and task descriptions
```

### Key Components

- **MapController**: Interface for platform-agnostic map operations
- **LocationService**: Interface for location tracking functionality
- **MapRepository**: For managing map state and persistence
- **LocationViewModel**: Manages location data and UI state

## Getting Started

### Prerequisites

- Android Studio Arctic Fox or later
- Xcode 13 or later (for iOS development)
- JDK 11 or later
- Kotlin 1.6.0 or later

### Setup

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/OpenSea.git
   ```

2. Open the project in Android Studio

3. Sync Gradle files

4. Run the application on your preferred platform:
   - For Android: Select the androidApp configuration
   - For iOS: Open the iosApp project in Xcode and run

## Development Workflow

The project follows a phased development approach:

1. **Project Setup**: Basic KMM configuration and architecture
2. **Map Integration**: Implementation of mapping capabilities
3. **Location Services**: Real-time location tracking
4. **Navigation UI**: User interface for navigation features
5. **Testing and Refinement**: Quality assurance and optimization

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- OpenStreetMap for providing map data
- The Kotlin Multiplatform community for tools and support