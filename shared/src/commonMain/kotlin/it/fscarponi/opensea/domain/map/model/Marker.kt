package it.fscarponi.opensea.domain.map.model

/**
 * Data class representing a map marker with various properties.
 *
 * @property id Unique identifier for the marker
 * @property position Geographical position of the marker
 * @property title Optional title/name for the marker
 * @property description Optional description for the marker
 * @property iconType Type of icon to display for the marker
 */
data class Marker(
    val id: String,
    val position: LatLng,
    val title: String? = null,
    val description: String? = null,
    val iconType: MarkerIconType = MarkerIconType.DEFAULT
)

/**
 * Enum representing different types of marker icons. Each type can be
 * styled differently on the map.
 */
enum class MarkerIconType {
    DEFAULT,
    ANCHOR,
    WAYPOINT,
    DANGER
}