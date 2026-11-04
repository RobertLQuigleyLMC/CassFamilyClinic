package tech.profquigley.cassfamilyclinic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/** Response body of the backend's `/events` endpoint. */
@Serializable
internal data class EventsResponse(val events: List<Event> = emptyList())

@Serializable
internal data class Event(
    val title: String,
    val url: String,
    @SerialName("start_date") val startDate: String,
    @SerialName("end_date") val endDate: String,
    // The upstream events API sends `[]` instead of an object when an event has no venue.
    @SerialName("venue") private val rawVenue: JsonElement? = null,
) {
    val venue: Venue?
        get() = (rawVenue as? JsonObject)?.let { eventsJson.decodeFromJsonElement(Venue.serializer(), it) }
}

@Serializable
internal data class Venue(
    val venue: String? = null,
    val address: String? = null,
    val city: String? = null,
    val state: String? = null,
    @SerialName("stateprovince") val stateProvince: String? = null,
    val zip: String? = null,
) {
    /** e.g. "Three Oaks, MI 49128"; parts that are missing are omitted. */
    val cityLine: String?
        get() {
            val region = (state ?: stateProvince)?.takeIf { it.isNotBlank() }
            val cityState = listOfNotNull(city?.takeIf { it.isNotBlank() }, region).joinToString(", ")
            return listOf(cityState, zip.orEmpty()).filter { it.isNotBlank() }.joinToString(" ").ifBlank { null }
        }
}

/**
 * Formats a server timestamp like "2026-11-04 16:00:00" as "11/04/2026 04:00 PM".
 * Returns the input unchanged if it isn't in the expected format.
 */
internal fun formatEventDateTime(raw: String): String {
    val match = Regex("""(\d{4})-(\d{2})-(\d{2})[ T](\d{2}):(\d{2})""").find(raw) ?: return raw
    val (year, month, day, hourStr, minute) = match.destructured
    val hour = hourStr.toInt()
    val hour12 = (if (hour % 12 == 0) 12 else hour % 12).toString().padStart(2, '0')
    val amPm = if (hour < 12) "AM" else "PM"
    return "$month/$day/$year $hour12:$minute $amPm"
}
