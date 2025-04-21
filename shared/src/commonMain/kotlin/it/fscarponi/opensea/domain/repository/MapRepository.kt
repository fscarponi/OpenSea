package it.fscarponi.opensea.domain.repository

import it.fscarponi.opensea.domain.map.model.LatLng
import it.fscarponi.opensea.domain.map.model.MapRegion

/**
 * Repository interface for map-related operations.
 * Handles saving and retrieving map state and caching map data.
 */
interface MapRepository : Repository {
    
    /**
     * Saves the current map center position.
     *
     * @param center The center position to save.
     */
    suspend fun saveMapCenter(center: LatLng)
    
    /**
     * Retrieves the last saved map center position.
     *
     * @return The last saved center position, or a default position if none is saved.
     */
    suspend fun getMapCenter(): LatLng
    
    /**
     * Saves the current map zoom level.
     *
     * @param zoomLevel The zoom level to save.
     */
    suspend fun saveZoomLevel(zoomLevel: Float)
    
    /**
     * Retrieves the last saved zoom level.
     *
     * @return The last saved zoom level, or a default level if none is saved.
     */
    suspend fun getZoomLevel(): Float
    
    /**
     * Saves a map region for later retrieval.
     *
     * @param name A unique name to identify the region.
     * @param region The map region to save.
     */
    suspend fun saveRegion(name: String, region: MapRegion)
    
    /**
     * Retrieves a saved map region by name.
     *
     * @param name The unique name of the region to retrieve.
     * @return The saved region, or null if no region with the given name exists.
     */
    suspend fun getRegion(name: String): MapRegion?
    
    /**
     * Retrieves all saved region names.
     *
     * @return A list of all saved region names.
     */
    suspend fun getSavedRegionNames(): List<String>
    
    /**
     * Clears all saved map data.
     */
    suspend fun clearMapData()
}