package it.fscarponi.opensea.domain.map

import it.fscarponi.opensea.domain.map.event.MapEvent
import it.fscarponi.opensea.domain.map.model.LatLng
import it.fscarponi.opensea.domain.map.model.MapConfiguration
import it.fscarponi.opensea.domain.map.model.MapRegion

/**
 * Platform-agnostic interface for controlling map operations.
 * This interface abstracts away platform-specific map implementations
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
     * Adds a marker at the specified geographical location.
     *
     * @param latitude The latitude coordinate in degrees.
     * @param longitude The longitude coordinate in degrees.
     * @return A unique identifier for the added marker.
     */
    fun addMarker(latitude: Double, longitude: Double): String

    /**
     * Adds a marker at the specified geographical location.
     *
     * @param location The geographical location for the marker.
     * @return A unique identifier for the added marker.
     */
    fun addMarker(location: LatLng): String {
        return addMarker(location.latitude, location.longitude)
    }

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
}
