package it.fscarponi.opensea.domain.location.model

/**
 * Data class representing a geographic location with additional metadata.
 *
 * @property latitude The latitude coordinate in degrees.
 * @property longitude The longitude coordinate in degrees.
 * @property altitude The altitude in meters above sea level, if available.
 * @property speed The speed in meters per second, if available.
 * @property bearing The bearing in degrees, if available.
 * @property accuracy The estimated accuracy of the location in meters, if available.
 * @property timestamp The timestamp when the location was determined, in milliseconds since epoch.
 */
data class Location(
    val latitude: Double,
    val longitude: Double,
    val altitude: Double? = null,
    val speed: Float? = null,
    val bearing: Float? = null,
    val accuracy: Float? = null,
    val timestamp: Long
)