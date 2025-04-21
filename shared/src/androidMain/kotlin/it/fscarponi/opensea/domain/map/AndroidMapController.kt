package it.fscarponi.opensea.domain.map

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import it.fscarponi.opensea.domain.map.event.MapEvent
import it.fscarponi.opensea.domain.map.model.LatLng
import it.fscarponi.opensea.domain.map.model.MapConfiguration
import it.fscarponi.opensea.domain.map.model.MapRegion
import it.fscarponi.opensea.domain.map.model.Marker
import it.fscarponi.opensea.domain.map.model.MarkerIconType
import org.maplibre.android.MapLibre
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.offline.OfflineManager
import org.maplibre.android.offline.OfflineRegion
import org.maplibre.android.offline.OfflineTilePyramidRegionDefinition
import org.maplibre.android.plugins.annotation.SymbolManager
import org.maplibre.android.plugins.annotation.SymbolOptions
import org.maplibre.android.plugins.cluster.clustering.ClusterManagerPlugin
import java.util.UUID

/**
 * Android-specific implementation of the [MapController] interface using MapLibre.
 *
 * @property mapView The MapLibre MapView instance to control.
 */
class AndroidMapController(private val mapView: MapView) : MapController {

    private var eventCallback: ((MapEvent) -> Unit)? = null
    private val markers = mutableMapOf<String, org.maplibre.android.annotations.Marker>()
    private val markerData = mutableMapOf<String, Marker>()

    private var clusterManager: ClusterManagerPlugin? = null
    private var symbolManager: SymbolManager? = null
    private var offlineManager: OfflineManager? = null

    private var isClusteringEnabled = false
    private var clusterRadius = 100

    // Map styling properties
    private var nightModeEnabled = false
    private var marineStyleEnabled = false
    private var tileCacheEnabled = false
    private var maxTileCacheSizeMB = 100

    // Style URLs
    private val dayStyleUrl = "https://demotiles.maplibre.org/style.json"
    private val nightStyleUrl = "https://demotiles.maplibre.org/style.json" // Would be replaced with actual night style
    private val marineStyleUrl = "https://demotiles.maplibre.org/style.json" // Would be replaced with marine style

    @SuppressLint("MissingPermission")
    override fun initialize(config: MapConfiguration) {
        // Save configuration settings
        this.nightModeEnabled = config.nightModeEnabled
        this.marineStyleEnabled = config.marineStyleEnabled
        this.tileCacheEnabled = config.enableTileCache
        this.maxTileCacheSizeMB = config.maxTileCacheSizeMB

        mapView.getMapAsync { map ->
            // Apply configuration
            val initialPosition = CameraPosition.Builder()
                .target(org.maplibre.android.geometry.LatLng(
                    config.initialCenter.latitude,
                    config.initialCenter.longitude
                ))
                .zoom(config.initialZoomLevel)
                .build()

            map.cameraPosition = initialPosition

            // Set min/max zoom levels
            map.setMinZoomPreference(config.minZoomLevel)
            map.setMaxZoomPreference(config.maxZoomLevel)

            // Determine which style to use based on configuration
            val styleUrl = when {
                config.mapStyle != null -> config.mapStyle
                config.nightModeEnabled -> nightStyleUrl
                config.marineStyleEnabled -> marineStyleUrl
                else -> dayStyleUrl
            }

            // Apply the selected style
            map.setStyle(styleUrl) { style ->
                // Initialize symbol manager for advanced marker management
                symbolManager = SymbolManager(mapView, map, style)

                // Initialize cluster manager if clustering is enabled
                if (config.enableTileCache) {
                    // Initialize offline manager for tile caching
                    offlineManager = OfflineManager.getInstance(mapView.context)

                    // Configure tile cache size
                    offlineManager?.setMaximumAmbientCacheSize(config.maxTileCacheSizeMB.toLong() * 1024 * 1024)
                }

                // Add OSM attribution if required
                if (config.osmAttributionEnabled) {
                    // In a real implementation, we would add OSM attribution to the map
                    // This is a placeholder for that functionality
                    println("OSM Attribution enabled")
                }

                // Notify that tiles are loaded
                eventCallback?.invoke(MapEvent.TilesLoaded)
            }

            // Configure UI settings
            map.uiSettings.apply {
                isCompassEnabled = config.showCompass
                isScaleBarEnabled = config.showScaleBar
            }

            // Enable location if requested
            if (config.showUserLocation) {
                map.locationComponent.apply {
                    activateLocationComponent(map.style!!)
                    isLocationComponentEnabled = true
                }
            }

            // Set up event listeners
            map.addOnMapClickListener { point ->
                val latLng = LatLng(point.latitude, point.longitude)
                eventCallback?.invoke(MapEvent.MapClick(latLng))
                false
            }

            // Add long press listener for adding markers
            map.addOnMapLongClickListener { point ->
                val latLng = LatLng(point.latitude, point.longitude)
                eventCallback?.invoke(MapEvent.MapLongPress(latLng))
                true
            }

            map.addOnCameraMoveListener {
                val visibleBounds = map.projection.visibleRegion.latLngBounds
                val visibleRegion = MapRegion(
                    northEast = LatLng(
                        visibleBounds.northeast.latitude,
                        visibleBounds.northeast.longitude
                    ),
                    southWest = LatLng(
                        visibleBounds.southwest.latitude,
                        visibleBounds.southwest.longitude
                    )
                )
                eventCallback?.invoke(MapEvent.CameraMove(visibleRegion, map.cameraPosition.zoom))
            }

            // Notify when map style starts loading
            map.addOnStyleLoadingListener {
                eventCallback?.invoke(MapEvent.TilesLoading)
            }

            // Notify that map is loaded
            map.addOnDidFinishLoadingStyleListener {
                eventCallback?.invoke(MapEvent.MapLoaded)
                eventCallback?.invoke(
                    MapEvent.StyleChanged(
                        if (nightModeEnabled) "night" else if (marineStyleEnabled) "marine" else "day"
                    )
                )
            }
        }
    }

    override fun setEventCallback(callback: (MapEvent) -> Unit) {
        this.eventCallback = callback
    }

    override fun centerOnLocation(latitude: Double, longitude: Double) {
        mapView.getMapAsync { map ->
            val position = CameraPosition.Builder()
                .target(org.maplibre.android.geometry.LatLng(latitude, longitude))
                .zoom(map.cameraPosition.zoom)
                .build()
            map.cameraPosition = position
        }
    }

    override fun addMarker(latitude: Double, longitude: Double): String {
        // Create a basic marker with default properties
        val marker = Marker(
            id = UUID.randomUUID().toString(),
            position = LatLng(latitude, longitude)
        )

        // Add the marker using the full implementation
        return addMarker(marker)
    }

    override fun addMarker(marker: Marker): String {
        val markerId = marker.id

        mapView.getMapAsync { map ->
            // Store the marker data
            markerData[markerId] = marker

            if (isClusteringEnabled && clusterManager != null) {
                // Add to cluster manager if clustering is enabled
                // This is a simplified implementation
                println("Adding marker to cluster manager: $markerId")
            } else {
                // Add as a regular marker
                val mapLibreMarker = map.addMarker(
                    MarkerOptions()
                        .position(
                            org.maplibre.android.geometry.LatLng(
                                marker.position.latitude,
                                marker.position.longitude
                            )
                        )
                        .title(marker.title)
                        .snippet(marker.description)
                )

                // Store the marker reference
                markers[markerId] = mapLibreMarker

                // Set up marker click listener if not already set
                if (markers.size == 1) {
                    map.setOnMarkerClickListener { clickedMarker ->
                        val id = markers.entries.find { it.value == clickedMarker }?.key
                        if (id != null) {
                            val position = LatLng(
                                clickedMarker.position.latitude,
                                clickedMarker.position.longitude
                            )
                            eventCallback?.invoke(MapEvent.MarkerClick(id, position))
                        }
                        false
                    }
                }
            }
        }

        return markerId
    }

    override fun updateMarker(marker: Marker): Boolean {
        // Check if the marker exists
        if (!markerData.containsKey(marker.id)) {
            return false
        }

        // Update the stored marker data
        markerData[marker.id] = marker

        mapView.getMapAsync { map ->
            // Get the existing marker
            val existingMarker = markers[marker.id]

            if (existingMarker != null) {
                // Update position
                existingMarker.position = org.maplibre.android.geometry.LatLng(
                    marker.position.latitude,
                    marker.position.longitude
                )

                // Update title and description
                existingMarker.title = marker.title
                existingMarker.snippet = marker.description

                // In a real implementation, we would update the icon based on marker.iconType
            }
        }

        return true
    }

    override fun removeMarker(markerId: String): Boolean {
        // Check if the marker exists
        if (!markerData.containsKey(markerId)) {
            return false
        }

        // Remove from data store
        markerData.remove(markerId)

        mapView.getMapAsync { map ->
            // Get the existing marker
            val existingMarker = markers[markerId]

            if (existingMarker != null) {
                // Remove from map
                existingMarker.remove()

                // Remove from markers map
                markers.remove(markerId)
            }
        }

        return true
    }

    override fun getMarkers(): List<Marker> {
        return markerData.values.toList()
    }

    override fun setMarkerClustering(enabled: Boolean, clusterRadius: Int) {
        this.isClusteringEnabled = enabled
        this.clusterRadius = clusterRadius

        mapView.getMapAsync { map ->
            if (enabled) {
                // Initialize cluster manager if not already initialized
                if (clusterManager == null) {
                    clusterManager = ClusterManagerPlugin(mapView.context, map)

                    // Set up cluster click listener
                    clusterManager?.setOnClusterClickListener { cluster ->
                        val position = LatLng(cluster.position.latitude, cluster.position.longitude)
                        val clusterItems = cluster.items.toList()
                        val markerIds = clusterItems.map { it.id.toString() }

                        eventCallback?.invoke(
                            MapEvent.ClusterClick(
                                clusterSize = cluster.size,
                                position = position,
                                markerIds = markerIds
                            )
                        )

                        true
                    }
                }

                // Configure cluster radius
                clusterManager?.algorithm?.maxDistanceBetweenClusteredItems = clusterRadius

                // Add existing markers to cluster manager
                // This is a simplified implementation
                println("Adding ${markerData.size} markers to cluster manager")
            } else {
                // Disable clustering
                clusterManager = null

                // Re-add all markers as regular markers
                markerData.values.forEach { marker ->
                    val mapLibreMarker = map.addMarker(
                        MarkerOptions()
                            .position(
                                org.maplibre.android.geometry.LatLng(
                                    marker.position.latitude,
                                    marker.position.longitude
                                )
                            )
                            .title(marker.title)
                            .snippet(marker.description)
                    )

                    markers[marker.id] = mapLibreMarker
                }
            }
        }
    }

    override fun setZoomLevel(level: Float) {
        mapView.getMapAsync { map ->
            val position = CameraPosition.Builder()
                .target(map.cameraPosition.target)
                .zoom(level)
                .build()
            map.cameraPosition = position
        }
    }

    override fun getVisibleRegion(): MapRegion {
        var region = MapRegion(
            northEast = LatLng(0.0, 0.0),
            southWest = LatLng(0.0, 0.0)
        )

        mapView.getMapAsync { map ->
            val bounds = map.projection.visibleRegion.latLngBounds
            region = MapRegion(
                northEast = LatLng(
                    bounds.northeast.latitude,
                    bounds.northeast.longitude
                ),
                southWest = LatLng(
                    bounds.southwest.latitude,
                    bounds.southwest.longitude
                )
            )
        }

        return region
    }

    override fun getZoomLevel(): Float {
        var zoomLevel = 0f

        mapView.getMapAsync { map ->
            zoomLevel = map.cameraPosition.zoom
        }

        return zoomLevel
    }

    override fun setScaleBarVisible(visible: Boolean) {
        mapView.getMapAsync { map ->
            map.uiSettings.isScaleBarEnabled = visible
        }
    }

    override fun setCompassVisible(visible: Boolean) {
        mapView.getMapAsync { map ->
            map.uiSettings.isCompassEnabled = visible
        }
    }

    override fun setNightMode(nightModeEnabled: Boolean) {
        // Only update if the mode is changing
        if (this.nightModeEnabled != nightModeEnabled) {
            this.nightModeEnabled = nightModeEnabled

            mapView.getMapAsync { map ->
                // Determine which style to use
                val styleUrl = if (nightModeEnabled) {
                    nightStyleUrl
                } else if (marineStyleEnabled) {
                    marineStyleUrl
                } else {
                    dayStyleUrl
                }

                // Apply the new style
                map.setStyle(styleUrl) { style ->
                    // Notify that style has changed
                    eventCallback?.invoke(
                        MapEvent.StyleChanged(
                            if (nightModeEnabled) "night" else if (marineStyleEnabled) "marine" else "day"
                        )
                    )
                }
            }
        }
    }

    override fun setMarineStyle(enabled: Boolean) {
        // Only update if the mode is changing
        if (this.marineStyleEnabled != enabled) {
            this.marineStyleEnabled = enabled

            mapView.getMapAsync { map ->
                // Determine which style to use
                val styleUrl = if (nightModeEnabled) {
                    nightStyleUrl
                } else if (enabled) {
                    marineStyleUrl
                } else {
                    dayStyleUrl
                }

                // Apply the new style
                map.setStyle(styleUrl) { style ->
                    // Notify that style has changed
                    eventCallback?.invoke(
                        MapEvent.StyleChanged(
                            if (nightModeEnabled) "night" else if (enabled) "marine" else "day"
                        )
                    )
                }
            }
        }
    }

    override fun setTileCache(enabled: Boolean, maxCacheSizeMB: Int) {
        this.tileCacheEnabled = enabled
        this.maxTileCacheSizeMB = maxCacheSizeMB

        mapView.getMapAsync { map ->
            if (enabled) {
                // Initialize offline manager if not already initialized
                if (offlineManager == null) {
                    offlineManager = OfflineManager.getInstance(mapView.context)
                }

                // Configure tile cache size
                offlineManager?.setMaximumAmbientCacheSize(maxCacheSizeMB.toLong() * 1024 * 1024)
            } else {
                // Clear the cache if disabling
                offlineManager?.resetDatabase { error ->
                    if (error != null) {
                        eventCallback?.invoke(MapEvent.Error("Failed to reset tile cache: ${error.message}"))
                    }
                }
            }
        }
    }

    override fun cacheRegion(region: MapRegion, minZoom: Float, maxZoom: Float) {
        if (!tileCacheEnabled || offlineManager == null) {
            eventCallback?.invoke(MapEvent.Error("Tile caching is not enabled"))
            return
        }

        mapView.getMapAsync { map ->
            // Get the current style URL
            val styleUrl = map.style?.url ?: dayStyleUrl

            // Create a bounding box for the region
            val bounds = LatLngBounds.Builder()
                .include(
                    org.maplibre.android.geometry.LatLng(
                        region.northEast.latitude,
                        region.northEast.longitude
                    )
                )
                .include(
                    org.maplibre.android.geometry.LatLng(
                        region.southWest.latitude,
                        region.southWest.longitude
                    )
                )
                .build()

            // Create an offline region definition
            val definition = OfflineTilePyramidRegionDefinition(
                styleUrl,
                bounds,
                minZoom,
                maxZoom,
                mapView.context.resources.displayMetrics.density
            )

            // Create metadata for the region
            val metadata = "{\"name\":\"Cached Region\"}"

            // Download the region
            offlineManager?.createOfflineRegion(definition, metadata.toByteArray()) { offlineRegion ->
                if (offlineRegion != null) {
                    // Start the download
                    offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE)

                    // Set up a progress listener
                    offlineRegion.setObserver(object : OfflineRegion.OfflineRegionObserver {
                        override fun onStatusChanged(status: OfflineRegionStatus) {
                            // Check if the download is complete
                            if (status.isComplete) {
                                eventCallback?.invoke(MapEvent.TilesLoaded)
                                offlineRegion.setDownloadState(OfflineRegion.STATE_INACTIVE)
                            }
                        }

                        override fun onError(error: OfflineRegionError) {
                            eventCallback?.invoke(MapEvent.Error("Tile caching error: ${error.message}"))
                        }

                        override fun mapboxTileCountLimitExceeded(limit: Long) {
                            eventCallback?.invoke(MapEvent.Error("Tile limit exceeded: $limit"))
                        }
                    })
                } else {
                    eventCallback?.invoke(MapEvent.Error("Failed to create offline region"))
                }
            }
        }
    }

    override fun clearTileCache() {
        if (offlineManager == null) {
            return
        }

        offlineManager?.resetDatabase { error ->
            if (error != null) {
                eventCallback?.invoke(MapEvent.Error("Failed to clear tile cache: ${error.message}"))
            }
        }
    }
}
