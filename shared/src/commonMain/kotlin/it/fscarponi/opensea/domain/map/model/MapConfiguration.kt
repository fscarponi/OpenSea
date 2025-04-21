package it.fscarponi.opensea.domain.map.model

/**
 * Configuration options for initializing and configuring a map.
 *
 * @property initialCenter The initial center position of the map.
 * @property initialZoomLevel The initial zoom level of the map.
 * @property minZoomLevel The minimum allowed zoom level.
 * @property maxZoomLevel The maximum allowed zoom level.
 * @property showUserLocation Whether to show the user's current location on the map.
 * @property showCompass Whether to show the compass on the map.
 * @property showScaleBar Whether to show the scale bar on the map.
 * @property mapStyle The style URL for the map (e.g., for MapLibre/Mapbox styles).
 */
data class MapConfiguration(
    val initialCenter: LatLng = LatLng(0.0, 0.0),
    val initialZoomLevel: Float = 10f,
    val minZoomLevel: Float = 0f,
    val maxZoomLevel: Float = 20f,
    val showUserLocation: Boolean = false,
    val showCompass: Boolean = true,
    val showScaleBar: Boolean = true,
    val mapStyle: String? = null
)