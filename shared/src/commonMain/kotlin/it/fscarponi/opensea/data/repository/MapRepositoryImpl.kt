package it.fscarponi.opensea.data.repository

import it.fscarponi.opensea.domain.map.model.LatLng
import it.fscarponi.opensea.domain.map.model.MapRegion
import it.fscarponi.opensea.domain.repository.MapRepository
import it.fscarponi.opensea.util.Logger

/**
 * Implementation of [MapRepository] that provides map data storage and retrieval.
 * Currently uses in-memory caching, with persistence to be added in a future phase.
 *
 * @property logger Logger for debugging and error reporting.
 */
class MapRepositoryImpl(private val logger: Logger) : MapRepository {

    // In-memory cache for map data
    private var cachedCenter: LatLng? = null
    private var cachedZoomLevel: Float? = null
    private val cachedRegions = mutableMapOf<String, MapRegion>()

    // Default values
    private val defaultCenter = LatLng(0.0, 0.0)
    private val defaultZoomLevel = 10f

    override suspend fun saveMapCenter(center: LatLng) {
        logger.debug("MapRepository", "Saving map center: $center")
        cachedCenter = center
        // In a future phase, this would also persist to storage
    }

    override suspend fun getMapCenter(): LatLng {
        val center = cachedCenter ?: defaultCenter
        logger.debug("MapRepository", "Retrieved map center: $center")
        return center
    }

    override suspend fun saveZoomLevel(zoomLevel: Float) {
        logger.debug("MapRepository", "Saving zoom level: $zoomLevel")
        cachedZoomLevel = zoomLevel
        // In a future phase, this would also persist to storage
    }

    override suspend fun getZoomLevel(): Float {
        val zoomLevel = cachedZoomLevel ?: defaultZoomLevel
        logger.debug("MapRepository", "Retrieved zoom level: $zoomLevel")
        return zoomLevel
    }

    override suspend fun saveRegion(name: String, region: MapRegion) {
        logger.debug("MapRepository", "Saving region '$name': $region")
        cachedRegions[name] = region
        // In a future phase, this would also persist to storage
    }

    override suspend fun getRegion(name: String): MapRegion? {
        val region = cachedRegions[name]
        logger.debug("MapRepository", "Retrieved region '$name': $region")
        return region
    }

    override suspend fun getSavedRegionNames(): List<String> {
        val names = cachedRegions.keys.toList()
        logger.debug("MapRepository", "Retrieved ${names.size} saved region names")
        return names
    }

    override suspend fun clearMapData() {
        logger.debug("MapRepository", "Clearing all map data")
        cachedCenter = null
        cachedZoomLevel = null
        cachedRegions.clear()
        // In a future phase, this would also clear persisted storage
    }
}
