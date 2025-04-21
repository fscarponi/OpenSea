package it.fscarponi.opensea.domain.map.model

/**
 * Configuration options for initializing and configuring a map.
 *
 * @property initialCenter The initial center position of the map.
 * @property initialZoomLevel The initial zoom level of the map.
 * @property minZoomLevel The minimum allowed zoom level.
 * @property maxZoomLevel The maximum allowed zoom level.
 * @property showUserLocation Whether to show the user's current location
 *    on the map.
 * @property showCompass Whether to show the compass on the map.
 * @property showScaleBar Whether to show the scale bar on the map.
 * @property mapStyle The style URL for the map (e.g., for MapLibre/Mapbox
 *    styles).
 * @property enableTileCache Whether to enable caching of map tiles for
 *    offline use.
 * @property maxTileCacheSizeMB Maximum size of the tile cache in
 *    megabytes.
 * @property nightModeEnabled Whether to use night mode styling for the
 *    map.
 * @property marineStyleEnabled Whether to use marine-specific styling for
 *    the map.
 * @property osmAttributionEnabled Whether to show OpenStreetMap
 *    attribution (required when using OSM tiles).
 */
data class MapConfiguration(
    val initialCenter: LatLng = LatLng(0.0, 0.0),
    val initialZoomLevel: Float = 10f,
    val minZoomLevel: Float = 0f,
    val maxZoomLevel: Float = 20f,
    val showUserLocation: Boolean = false,
    val showCompass: Boolean = true,
    val showScaleBar: Boolean = true,
    val mapStyle: String? = "https://demotiles.maplibre.org/style.json", // Default to OSM-based style
    val enableTileCache: Boolean = true,
    val maxTileCacheSizeMB: Int = 100,
    val nightModeEnabled: Boolean = false,
    val marineStyleEnabled: Boolean = true,
    val osmAttributionEnabled: Boolean = true
)
