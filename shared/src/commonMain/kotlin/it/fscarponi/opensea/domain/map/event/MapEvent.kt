package it.fscarponi.opensea.domain.map.event

import it.fscarponi.opensea.domain.map.model.LatLng
import it.fscarponi.opensea.domain.map.model.MapRegion

/**
 * Sealed class representing various events that can occur on a map. This
 * provides a type-safe way to handle different map events.
 */
sealed class MapEvent {
    /**
     * Event triggered when the map is clicked/tapped.
     *
     * @property position The geographical position where the map was clicked.
     */
    data class MapClick(val position: LatLng) : MapEvent()

    /**
     * Event triggered when the map is long-pressed. This is typically used for
     * adding markers or other interactive elements.
     *
     * @property position The geographical position where the map was
     *    long-pressed.
     */
    data class MapLongPress(val position: LatLng) : MapEvent()

    /**
     * Event triggered when a marker on the map is clicked/tapped.
     *
     * @property markerId The unique identifier of the marker that was clicked.
     * @property position The geographical position of the marker.
     */
    data class MarkerClick(val markerId: String, val position: LatLng) : MapEvent()

    /**
     * Event triggered when a marker cluster is clicked. A cluster represents
     * multiple markers that are grouped together at high zoom levels.
     *
     * @property clusterSize The number of markers in the cluster.
     * @property position The geographical position of the cluster.
     * @property markerIds List of marker IDs contained in this cluster.
     */
    data class ClusterClick(
        val clusterSize: Int,
        val position: LatLng,
        val markerIds: List<String>
    ) : MapEvent()

    /**
     * Event triggered when the camera position changes (e.g., after panning or
     * zooming).
     *
     * @property visibleRegion The new visible region on the map.
     * @property zoomLevel The new zoom level.
     */
    data class CameraMove(val visibleRegion: MapRegion, val zoomLevel: Float) : MapEvent()

    /** Event triggered when the map has finished loading. */
    object MapLoaded : MapEvent()

    /** Event triggered when map tiles start loading. */
    object TilesLoading : MapEvent()

    /** Event triggered when map tiles have finished loading. */
    object TilesLoaded : MapEvent()

    /**
     * Event triggered when the map style changes (e.g., switching between
     * day/night mode).
     *
     * @property styleName The name or identifier of the new style.
     */
    data class StyleChanged(val styleName: String) : MapEvent()

    /**
     * Event triggered when an error occurs during map operations.
     *
     * @property message A description of the error.
     * @property code An optional error code.
     */
    data class Error(val message: String, val code: Int? = null) : MapEvent()
}
