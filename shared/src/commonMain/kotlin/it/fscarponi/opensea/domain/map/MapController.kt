package it.fscarponi.opensea.domain.map

import it.fscarponi.opensea.domain.map.event.MapEvent
import it.fscarponi.opensea.domain.map.model.LatLng
import it.fscarponi.opensea.domain.map.model.MapConfiguration
import it.fscarponi.opensea.domain.map.model.MapRegion
import it.fscarponi.opensea.domain.map.model.Marker

/**
 * Platform-agnostic interface for controlling map operations. This
 * interface abstracts away platform-specific map implementations
 * to provide a common API for map operations across platforms.
 */
interface MapController {
    /**
     * Initializes the map with the provided configuration.
     *
     * @param config The configuration options for the map.
     */
    fun initialize(config: MapConfiguration = MapConfiguration())

    /**
     * Sets a callback to be invoked when map events occur.
     *
     * @param callback The function to be called when a map event occurs.
     */
    fun setEventCallback(callback: (MapEvent) -> Unit)

    /**
     * Centers the map view on the specified geographical location.
     *
     * @param latitude The latitude coordinate in degrees.
     * @param longitude The longitude coordinate in degrees.
     */
    fun centerOnLocation(latitude: Double, longitude: Double)

    /**
     * Centers the map view on the specified geographical location.
     *
     * @param location The geographical location to center on.
     */
    fun centerOnLocation(location: LatLng) {
        centerOnLocation(location.latitude, location.longitude)
    }

    /**
     * Adds a basic marker at the specified geographical location.
     *
     * @param latitude The latitude coordinate in degrees.
     * @param longitude The longitude coordinate in degrees.
     * @return A unique identifier for the added marker.
     */
    fun addMarker(latitude: Double, longitude: Double): String

    /**
     * Adds a basic marker at the specified geographical location.
     *
     * @param location The geographical location for the marker.
     * @return A unique identifier for the added marker.
     */
    fun addMarker(location: LatLng): String {
        return addMarker(location.latitude, location.longitude)
    }

    /**
     * Adds a marker with the specified properties.
     *
     * @param marker The marker to add with all its properties.
     * @return The unique identifier of the added marker (same as marker.id).
     */
    fun addMarker(marker: Marker): String

    /**
     * Updates an existing marker with new properties.
     *
     * @param marker The marker with updated properties. The id must match an
     *    existing marker.
     * @return True if the marker was successfully updated, false otherwise.
     */
    fun updateMarker(marker: Marker): Boolean

    /**
     * Removes a marker from the map.
     *
     * @param markerId The unique identifier of the marker to remove.
     * @return True if the marker was successfully removed, false otherwise.
     */
    fun removeMarker(markerId: String): Boolean

    /**
     * Gets all markers currently displayed on the map.
     *
     * @return A list of all markers.
     */
    fun getMarkers(): List<Marker>

    /**
     * Enables or disables marker clustering. When enabled, markers that are
     * close together will be grouped into clusters at lower zoom levels.
     *
     * @param enabled Whether clustering should be enabled.
     * @param clusterRadius The radius (in pixels) within which markers will be
     *    clustered together.
     */
    fun setMarkerClustering(enabled: Boolean, clusterRadius: Int = 100)

    /**
     * Sets the zoom level of the map view.
     *
     * @param level The zoom level to set (typically between 0-20, where 0 is fully zoomed out).
     */
    fun setZoomLevel(level: Float)

    /**
     * Gets the currently visible region on the map.
     *
     * @return The visible region as a [MapRegion].
     */
    fun getVisibleRegion(): MapRegion

    /**
     * Gets the current zoom level of the map.
     *
     * @return The current zoom level.
     */
    fun getZoomLevel(): Float

    /**
     * Enables or disables the display of the scale bar.
     *
     * @param visible Whether the scale bar should be visible.
     */
    fun setScaleBarVisible(visible: Boolean)

    /**
     * Enables or disables the display of the compass.
     *
     * @param visible Whether the compass should be visible.
     */
    fun setCompassVisible(visible: Boolean)

    /**
     * Sets the map style to day or night mode.
     *
     * @param nightModeEnabled Whether night mode should be enabled.
     */
    fun setNightMode(nightModeEnabled: Boolean)

    /**
     * Enables or disables marine-specific styling for the map.
     *
     * @param enabled Whether marine styling should be enabled.
     */
    fun setMarineStyle(enabled: Boolean)

    /**
     * Enables or disables tile caching for offline use.
     *
     * @param enabled Whether tile caching should be enabled.
     * @param maxCacheSizeMB Maximum size of the tile cache in megabytes.
     */
    fun setTileCache(enabled: Boolean, maxCacheSizeMB: Int = 100)

    /**
     * Pre-caches map tiles for the specified region. This is useful for
     * ensuring offline availability of map data.
     *
     * @param region The geographical region to cache.
     * @param minZoom The minimum zoom level to cache.
     * @param maxZoom The maximum zoom level to cache.
     */
    fun cacheRegion(region: MapRegion, minZoom: Float, maxZoom: Float)

    /** Clears the tile cache. */
    fun clearTileCache()
}
