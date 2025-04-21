package it.fscarponi.opensea.domain.map

import android.annotation.SuppressLint
import it.fscarponi.opensea.domain.map.event.MapEvent
import it.fscarponi.opensea.domain.map.model.LatLng
import it.fscarponi.opensea.domain.map.model.MapConfiguration
import it.fscarponi.opensea.domain.map.model.MapRegion
import org.maplibre.android.MapLibre
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.annotations.MarkerOptions
import java.util.UUID

/**
 * Android-specific implementation of the [MapController] interface using MapLibre.
 *
 * @property mapView The MapLibre MapView instance to control.
 */
class AndroidMapController(private val mapView: MapView) : MapController {
    
    private var eventCallback: ((MapEvent) -> Unit)? = null
    private val markers = mutableMapOf<String, org.maplibre.android.annotations.Marker>()
    
    @SuppressLint("MissingPermission")
    override fun initialize(config: MapConfiguration) {
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
            
            // Apply map style if provided, otherwise use default
            val styleUrl = config.mapStyle ?: Style.MAPBOX_STREETS
            map.setStyle(styleUrl)
            
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
            
            // Notify that map is loaded
            map.addOnDidFinishLoadingStyleListener {
                eventCallback?.invoke(MapEvent.MapLoaded)
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
        val markerId = UUID.randomUUID().toString()
        
        mapView.getMapAsync { map ->
            val marker = map.addMarker(
                MarkerOptions()
                    .position(org.maplibre.android.geometry.LatLng(latitude, longitude))
            )
            
            markers[markerId] = marker
            
            // Set up marker click listener
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
        
        return markerId
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
}