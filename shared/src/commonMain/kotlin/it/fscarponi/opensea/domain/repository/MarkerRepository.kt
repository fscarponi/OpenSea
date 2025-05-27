package it.fscarponi.opensea.domain.repository

import it.fscarponi.opensea.domain.map.model.Marker
import it.fscarponi.opensea.domain.model.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing map markers.
 */
interface MarkerRepository : Repository {
    /**
     * Gets all markers as a flow.
     *
     * @return A flow of lists of markers.
     */
    fun getAllMarkers(): Flow<List<Marker>>
    
    /**
     * Gets a marker by its ID.
     *
     * @param id The ID of the marker to get.
     * @return A result containing the marker, or an error if the marker was not found.
     */
    suspend fun getMarkerById(id: String): Result<Marker>
    
    /**
     * Adds a marker.
     *
     * @param marker The marker to add.
     * @return A result indicating success or failure.
     */
    suspend fun addMarker(marker: Marker): Result<Unit>
    
    /**
     * Updates a marker.
     *
     * @param marker The marker to update.
     * @return A result indicating success or failure.
     */
    suspend fun updateMarker(marker: Marker): Result<Unit>
    
    /**
     * Deletes a marker.
     *
     * @param id The ID of the marker to delete.
     * @return A result indicating success or failure.
     */
    suspend fun deleteMarker(id: String): Result<Unit>
}