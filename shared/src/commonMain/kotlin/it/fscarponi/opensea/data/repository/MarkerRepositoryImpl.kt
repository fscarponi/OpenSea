package it.fscarponi.opensea.data.repository

import it.fscarponi.opensea.domain.map.MapController
import it.fscarponi.opensea.domain.map.model.Marker
import it.fscarponi.opensea.domain.model.Result
import it.fscarponi.opensea.domain.repository.MarkerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Implementation of [MarkerRepository] that stores markers in memory.
 *
 * @property mapController The map controller used to display markers on the map.
 */
class MarkerRepositoryImpl(
    private val mapController: MapController
) : MarkerRepository {
    
    private val markers = mutableMapOf<String, Marker>()
    private val markersFlow = MutableStateFlow<List<Marker>>(emptyList())
    
    override fun getAllMarkers(): Flow<List<Marker>> = markersFlow.asStateFlow()
    
    override suspend fun getMarkerById(id: String): Result<Marker> {
        val marker = markers[id]
        return if (marker != null) {
            Result.Success(marker)
        } else {
            Result.Error("Marker with ID $id not found")
        }
    }
    
    override suspend fun addMarker(marker: Marker): Result<Unit> {
        markers[marker.id] = marker
        updateMarkersFlow()
        return Result.Success(Unit)
    }
    
    override suspend fun updateMarker(marker: Marker): Result<Unit> {
        if (markers.containsKey(marker.id)) {
            markers[marker.id] = marker
            updateMarkersFlow()
            return Result.Success(Unit)
        }
        return Result.Error("Marker with ID ${marker.id} not found")
    }
    
    override suspend fun deleteMarker(id: String): Result<Unit> {
        if (markers.remove(id) != null) {
            updateMarkersFlow()
            return Result.Success(Unit)
        }
        return Result.Error("Marker with ID $id not found")
    }
    
    /**
     * Updates the markers flow with the current list of markers.
     */
    private fun updateMarkersFlow() {
        markersFlow.value = markers.values.toList()
    }
}