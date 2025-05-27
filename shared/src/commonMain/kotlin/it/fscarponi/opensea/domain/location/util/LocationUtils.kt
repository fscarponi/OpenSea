package it.fscarponi.opensea.domain.location.util

import it.fscarponi.opensea.domain.location.model.Location
import kotlin.math.*
import kotlin.math.PI

/**
 * Utility functions for location-related calculations.
 */
object LocationUtils {
    /**
     * Earth radius in meters.
     */
    private const val EARTH_RADIUS = 6371000.0 // meters

    /**
     * Conversion factor from meters per second to knots.
     */
    private const val MPS_TO_KNOTS = 1.94384

    /**
     * Conversion factor from knots to kilometers per hour.
     */
    private const val KNOTS_TO_KMH = 1.852

    /**
     * Conversion factor from knots to miles per hour.
     */
    private const val KNOTS_TO_MPH = 1.15078

    /**
     * Converts speed from meters per second to knots.
     *
     * @param mps Speed in meters per second.
     * @return Speed in knots.
     */
    fun metersPerSecondToKnots(mps: Float): Double = mps * MPS_TO_KNOTS

    /**
     * Converts speed from knots to meters per second.
     *
     * @param knots Speed in knots.
     * @return Speed in meters per second.
     */
    fun knotsToMetersPerSecond(knots: Double): Float = (knots / MPS_TO_KNOTS).toFloat()

    /**
     * Converts speed from knots to kilometers per hour.
     *
     * @param knots Speed in knots.
     * @return Speed in kilometers per hour.
     */
    fun knotsToKmh(knots: Double): Double = knots * KNOTS_TO_KMH

    /**
     * Converts speed from knots to miles per hour.
     *
     * @param knots Speed in knots.
     * @return Speed in miles per hour.
     */
    fun knotsToMph(knots: Double): Double = knots * KNOTS_TO_MPH

    /**
     * Calculates the distance between two points using the Haversine formula.
     *
     * @param lat1 Latitude of the first point in degrees.
     * @param lon1 Longitude of the first point in degrees.
     * @param lat2 Latitude of the second point in degrees.
     * @param lon2 Longitude of the second point in degrees.
     * @return Distance in meters.
     */
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val lat1Rad = lat1 * PI / 180.0
        val lon1Rad = lon1 * PI / 180.0
        val lat2Rad = lat2 * PI / 180.0
        val lon2Rad = lon2 * PI / 180.0

        val dLat = lat2Rad - lat1Rad
        val dLon = lon2Rad - lon1Rad

        val a = sin(dLat * 0.5).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(dLon * 0.5).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return EARTH_RADIUS * c
    }

    /**
     * Calculates the distance between two locations using the Haversine formula.
     *
     * @param location1 The first location.
     * @param location2 The second location.
     * @return Distance in meters.
     */
    fun calculateDistance(location1: Location, location2: Location): Double {
        return calculateDistance(
            location1.latitude, location1.longitude,
            location2.latitude, location2.longitude
        )
    }

    /**
     * Calculates the speed between two locations based on the distance and time difference.
     *
     * @param location1 The first location.
     * @param location2 The second location.
     * @return Speed in knots.
     */
    fun calculateSpeed(location1: Location, location2: Location): Double {
        val distance = calculateDistance(location1, location2)
        val timeDiff = (location2.timestamp - location1.timestamp) / 1000.0 // seconds

        if (timeDiff <= 0) return 0.0

        val speedMps = distance / timeDiff
        return metersPerSecondToKnots(speedMps.toFloat())
    }

    /**
     * Applies a simple smoothing algorithm to a list of speed readings.
     *
     * @param speeds List of speed readings in knots.
     * @return Smoothed speed in knots.
     */
    fun smoothSpeed(speeds: List<Double>): Double {
        if (speeds.isEmpty()) return 0.0
        if (speeds.size == 1) return speeds[0]

        // Remove outliers (values that are more than 2 standard deviations from the mean)
        val mean = speeds.average()
        val stdDev = sqrt(speeds.map { (it - mean).pow(2) }.average())
        val filteredSpeeds = speeds.filter { abs(it - mean) <= 2 * stdDev }

        return filteredSpeeds.takeIf { it.isNotEmpty() }?.average() ?: mean
    }
}

/**
 * Extension function to convert Float speed to knots.
 */
fun Float.toKnots(): Double = LocationUtils.metersPerSecondToKnots(this)
