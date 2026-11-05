package tech.profquigley.cassfamilyclinic

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/** Response body of the backend's `/locations` endpoint: the clinic's most recent reported fixes. */
@Serializable
internal data class LocationsResponse(val locations: List<JsonElement> = emptyList())

/** One GPS fix reported by the mobile clinic's tracker. */
@Serializable
internal data class LocationPoint(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Double = 0.0,
    val timestampMillis: Long = 0,
)

/** The mobile clinic's recent path, oldest point first. */
internal data class LocationTrail(val points: List<LocationPoint>)

/**
 * Converts a `/locations` response into a [LocationTrail]. The server stores whatever the tracker
 * PUTs, so entries that don't parse or have out-of-range coordinates are skipped rather than
 * failing the whole trail.
 */
internal fun LocationsResponse.toLocationTrail(): LocationTrail =
    LocationTrail(
        locations
            .mapNotNull { runCatching { eventsJson.decodeFromJsonElement(LocationPoint.serializer(), it) }.getOrNull() }
            .filter { it.latitude in -90.0..90.0 && it.longitude in -180.0..180.0 }
            .sortedBy { it.timestampMillis },
    )
