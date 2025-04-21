package it.fscarponi.opensea.domain.map.model

/**
 * Represents a rectangular region on a map defined by its northeast and southwest corners.
 *
 * @property northEast The northeast corner coordinate of the region.
 * @property southWest The southwest corner coordinate of the region.
 */
data class MapRegion(
    val northEast: LatLng,
    val southWest: LatLng
)