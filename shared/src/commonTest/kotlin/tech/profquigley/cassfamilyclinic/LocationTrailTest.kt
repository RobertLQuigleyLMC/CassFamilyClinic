package tech.profquigley.cassfamilyclinic

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LocationTrailTest {

    @Test
    fun convertsResponseToTrailSortedOldestFirst() {
        val json = """
            {"locations": [
              {"latitude": 41.80, "longitude": -86.60, "accuracy": 5.0, "timestampMillis": 3000, "extra": 1},
              {"latitude": 41.78, "longitude": -86.62, "accuracy": 8.0, "timestampMillis": 1000},
              {"latitude": 41.79, "longitude": -86.61, "timestampMillis": 2000}
            ]}
        """.trimIndent()
        val trail = eventsJson.decodeFromString(LocationsResponse.serializer(), json).toLocationTrail()
        assertEquals(listOf(1000L, 2000L, 3000L), trail.points.map { it.timestampMillis })
        assertEquals(LocationPoint(41.80, -86.60, 5.0, 3000), trail.points.last())
        assertEquals(0.0, trail.points[1].accuracy)
    }

    @Test
    fun skipsMalformedAndOutOfRangeEntries() {
        val json = """
            {"locations": [
              {"latitude": 41.80, "longitude": -86.60, "timestampMillis": 1},
              {"lat": 41.0, "lng": -86.0},
              "garbage",
              {"latitude": 91.0, "longitude": -86.60, "timestampMillis": 2}
            ]}
        """.trimIndent()
        val trail = eventsJson.decodeFromString(LocationsResponse.serializer(), json).toLocationTrail()
        assertEquals(1, trail.points.size)
    }

    @Test
    fun emptyResponseGivesEmptyTrail() {
        val trail = eventsJson.decodeFromString(LocationsResponse.serializer(), """{"locations": []}""").toLocationTrail()
        assertTrue(trail.points.isEmpty())
    }
}
