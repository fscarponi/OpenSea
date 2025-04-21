package it.fscarponi.opensea.domain.map.model

/**
 * Represents a geographical coordinate with latitude and longitude.
 *
 * @property latitude The latitude coordinate in degrees.
 * @property longitude The longitude coordinate in degrees.
 */
data class LatLng(
    val latitude: Double,
    val longitude: Double
)